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
	
	@Autowired
	private OcpiConfigService configService;

	private ConcurrentHashMap<String, Token> tokens = new ConcurrentHashMap<String, Token>();
	
	private final Object tokenSyncLock = new Object();
	
	private Boolean isPullingTokenFromServer = false;
	
	public Token findToken(String countryCode, String partyId, String tokenUid, TokenType type) {
		
		String key = String.format("%s:%s:%s%s", countryCode, partyId, tokenUid, type);
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
			logger.info("Token pull commences");
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
		
		try {
			String url = serverCredentialUrl.replace("/credentials", "/tokens/");
			logger.info("Get token url = " + url);
			
			// TODO: Should use do-while loop to go through all pages
			
			ResponseEntity<OcpiResponse<List<Token>>> httpResp = restClient.get()
					.uri(url, Map.of("limit", 50, "date_from", fromLast30Days))
					.header("Authorization", "Token " + encodedToken)
					.retrieve()
					.toEntity(new ParameterizedTypeReference<>() {});	// Let Java do the type inference
			
			List<String> header = httpResp.getHeaders().get("X-Total-Count");
			if (header != null && header.size() > 0) {
				logger.info("X-Total-Count = {}", httpResp.getHeaders().get("X-Total-Count").get(0));
			}
			
			header = httpResp.getHeaders().get("Link");
			if (header != null && header.size() > 0) {
				logger.info("Link = {}", httpResp.getHeaders().get("Link").get(0));
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
		}
		catch (Exception ex) {
			
			logger.error("Error when pulling tokens from server, abort");
			logger.error(ex.getMessage());
			ex.printStackTrace();
		}
		finally {
			logger.info("Processed {} tokens, now has {} tokens", tokenAdded, this.tokens.mappingCount());
			synchronized (this.tokenSyncLock) {
				this.isPullingTokenFromServer = false;
			}
			logger.info("Token pull statistics: tokenProcessed = {}, tokenAdded = {}, tokenIgnored = {}", tokenProcessed, tokenAdded, tokenIgnored);
		}
	}
}
