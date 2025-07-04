package org.sdf.jimfan.ocpiclient.service;

import org.sdf.jimfan.ocpiclient.model.datatype.TokenType;
import org.sdf.jimfan.ocpiclient.model.token.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Scope(value=WebApplicationContext.SCOPE_APPLICATION)
public class OcpiTokenService {

	private ConcurrentHashMap<String, Token> tokens = new ConcurrentHashMap<String, Token>();
	
	public Token findToken(String countryCode, String partyId, String tokenUid, TokenType type) {
		
		String key = String.format("%s:%s:%s", countryCode, partyId, tokenUid);
		Token result = tokens.get(key);
		if (result != null && result.getTokenType() == type) {
			return result;
		}
		return null;
	}
	
	public Token upsertToken(String countryCode, String partyId, String tokenUid, TokenType type, Token incomingToken) {
		
		String key = String.format("%s:%s:%s", countryCode, partyId, tokenUid);
		Token replaced = tokens.get(key);
		this.tokens.put(key, incomingToken);
		if (replaced == null) {
			return null;
		}
		else if (replaced.getTokenType() == type) {
			return replaced;
		}
		
		// Found token but type does not match, not replacement
		return null;
	}
}
