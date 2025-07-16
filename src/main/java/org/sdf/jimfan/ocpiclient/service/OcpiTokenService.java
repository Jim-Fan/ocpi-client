package org.sdf.jimfan.ocpiclient.service;

import org.sdf.jimfan.ocpiclient.model.OcpiResponse;
import org.sdf.jimfan.ocpiclient.model.datatype.TokenType;
import org.sdf.jimfan.ocpiclient.model.token.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Scope(value=WebApplicationContext.SCOPE_APPLICATION)
public class OcpiTokenService {
	
	private static final Logger logger = LoggerFactory.getLogger(OcpiTokenService.class);
	
	private static final int MAX_PULL_TOKEN_EXCEPTION = 3;
	
	@Autowired
	private OcpiConfigService configService;

	private ConcurrentHashMap<String, Token> tokens = new ConcurrentHashMap<String, Token>();
	
	private final Object tokenSyncLock = new Object();
	
	private Boolean isPullingTokenFromServer = false;
	
	public Token findToken(String countryCode, String partyId, String tokenUid, TokenType type) {
		
		String key = String.format("%s:%s:%s:%s", countryCode, partyId, tokenUid, type);
		Token result = this.tokens.get(key);
		if (result != null && result.getTokenType() == type) {
			return result;
		}
		return null;
	}
	
	public Token upsertToken(String countryCode, String partyId, String tokenUid, TokenType type, Token incomingToken) {

		// OCPI specification allows and expects concurrent token update. Make a note when this occurs.
		if (this.isPullingTokenFromServer) {
			logger.warn("Upserting token while batch pull is in progress");
		}
		
		String key = String.format("%s:%s:%s:%s", countryCode, partyId, tokenUid, type);
		Token replaced = this.tokens.get(key);
		
		if (replaced == null) {
			this.tokens.put(key, incomingToken);
			logger.info("New token inserted {}", incomingToken);
			return null;
		}
		else if (incomingToken.getLastUpdated().getTime() > replaced.getLastUpdated().getTime()) {
			this.tokens.put(key, incomingToken);
			logger.info("Token {} is replaced by {}", replaced, incomingToken);
			return replaced;
		}
		
		// TODO: No update, what to do? Returning null is wrong as it means new token inserted above
		logger.info("Incoming token {} is older than existing, not updating", incomingToken);
		return null;
	}
	
	public Collection<Token> getAllTokens() {
		return this.tokens.values();
	}
	
	@Async
	public void pullTokensFromServer() {
		
		synchronized (this.tokenSyncLock) {
			if (this.isPullingTokenFromServer) {
				logger.info("Token pull is in progress, do nothing and return");
				return;
			}
			logger.info("Token pull commencing");
			this.isPullingTokenFromServer = true;
		}
		
		String serverCredentialUrl = this.configService.getTheirOcpiCredentialsUrl();
		String partyId = this.configService.getMyOcpiPartyId();
		String countryCode = this.configService.getMyOcpiCountryCode();
		String encodedToken = this.configService.getMyOcpiCredentialTokenEncoded();
		
		RestClient restClient = RestClient.create();
		
		Date now = new Date();
		String fromLast30Days = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'").format(
				new Date(now.getTime() - (1000 * 60 * 60 * 24 * 30)));
		int tokenAdded = 0;
		int tokenProcessed = 0;
		int tokenIgnored = 0;
		
		// TODO: Get URL from version details instead
		String url = serverCredentialUrl.replace("/credentials", "/tokens/");
		Map<String, Object> getParams = new HashMap<String, Object>();
		getParams.put("limit", 200);
		getParams.put("date_from", fromLast30Days);
		
		int exceptionCount = 0;
		
		do {
			try {
				logger.info("Get token url = " + url);
				
				// TODO: Do not rely on sanity of link URL and total count returned by server
				
				ResponseEntity<OcpiResponse<List<Token>>> httpResp = restClient.get()
						.uri(url, getParams)
						.header("Authorization", "Token " + encodedToken)
						.retrieve()
						.toEntity(new ParameterizedTypeReference<>() {});	// Let Java do the type inference
				
				// Only need time range in first iteration
				getParams.remove("date_from");
				
				List<String> header = httpResp.getHeaders().get("X-Total-Count");
				int totalTokenCount = 0;
				if (header != null && header.size() > 0) {
					totalTokenCount = Integer.parseInt(header.get(0));
					logger.info("X-Total-Count = {}", totalTokenCount);
				}
				
				header = httpResp.getHeaders().get("Link");
				if (header != null && header.size() > 0) {
					
					// TODO: Handle parse error
					url = header.get(0);
					url = url.substring(url.indexOf("<") + 1, url.indexOf(">"));	
					logger.info("Next page URL = {}", httpResp.getHeaders().get("Link").get(0));					
				}
				else {
						url = null;
						logger.info("Next page URL not found in response header, this is last iteration");
				}
				
				header = httpResp.getHeaders().get("X-Limit");
				if (header != null && header.size() > 0) {
					logger.info("X-Limit = {}", httpResp.getHeaders().get("X-Limit").get(0));
				}
				
				OcpiResponse<List<Token>> ocpiResp = httpResp.getBody();
				List<Token> data = ocpiResp.getData();
				Iterator<Token> iter = data.iterator();
				
				logger.info("Token data acquired, updating local storage");
				
				// synchronized (this.tokens) {
				
				// this.tokens.clear();
				
				while (iter.hasNext()) {
					Token incomingToken = iter.next();
					String key = String.format("%s:%s:%s:%s", incomingToken.getCountryCode(), incomingToken.getPartyId(), incomingToken.getUid(), incomingToken.getTokenType());
					
					Token existing = this.tokens.get(key);
					if (existing == null) {
						this.tokens.put(key, incomingToken);
						++tokenAdded;
					}
					else if (incomingToken.getLastUpdated() != null) {
						if (incomingToken.getLastUpdated().getTime() > existing.getLastUpdated().getTime()) {
							this.tokens.put(key, incomingToken);
							++tokenAdded;
						}
						else {
							logger.warn("Incoming token {} is not newer then existing {}, ignored", incomingToken, existing);
							++tokenIgnored;
						}
					}
					else {
						logger.warn("Incoming token {} has null last updated field, ignored", incomingToken);
						++tokenIgnored;
					}
					
					++tokenProcessed;
					if (tokenProcessed % 100 == 0) {
						logger.info("Processed {} tokens...", tokenProcessed);
					}
	
					Thread.sleep(30);
				}
				
				// TODO: Device better infinite loop prevention
				if (tokenProcessed >= totalTokenCount) {
					logger.info("tokenProcessed ({}) >= totalTokenCount ({}), this is last iteration", tokenProcessed, totalTokenCount);
					url = null;
				}
			}
			catch (Exception ex) {
				++exceptionCount;
				logger.error("Error when pulling tokens from server, abort current page");
				logger.error(ex.getMessage());
				ex.printStackTrace();
				
				if (exceptionCount >= MAX_PULL_TOKEN_EXCEPTION) {
					logger.error("Too many exceptions ocurred ({}), abort whole operation", exceptionCount);
					url = null;
				}
			}
			finally {
				logger.info("Processed {} tokens, now has {} local tokens", tokenAdded, this.tokens.mappingCount());
			}
		}
		while (url != null);
		
		synchronized (this.tokenSyncLock) {
			this.isPullingTokenFromServer = false;
		}
		logger.info("Token pull statistics: tokenProcessed = {}, tokenAdded = {}, tokenIgnored = {}", tokenProcessed, tokenAdded, tokenIgnored);
	}
}
