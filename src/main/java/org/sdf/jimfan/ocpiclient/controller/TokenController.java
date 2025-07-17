package org.sdf.jimfan.ocpiclient.controller;

import org.sdf.jimfan.ocpiclient.model.OcpiResponse;
import org.sdf.jimfan.ocpiclient.model.datatype.EnergyContract;
import org.sdf.jimfan.ocpiclient.model.datatype.LanguageCode;
import org.sdf.jimfan.ocpiclient.model.datatype.ProfileType;
import org.sdf.jimfan.ocpiclient.model.datatype.TokenType;
import org.sdf.jimfan.ocpiclient.model.datatype.WhitelistType;
import org.sdf.jimfan.ocpiclient.model.token.Token;
import org.sdf.jimfan.ocpiclient.service.OcpiTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;

/**
 * OCPI 2.2.1 section 12.2
 */
@RestController
@EnableAutoConfiguration
@ComponentScan
@Scope(value=WebApplicationContext.SCOPE_APPLICATION)
public class TokenController {

	private final Logger logger = LoggerFactory.getLogger(TokenController.class);
	
	@Autowired
	private OcpiTokenService tokenService;
	
	@GetMapping("/ocpi/2.2.1/tokens/{countryCode}/{partyId}/{tokenUid}")
	public OcpiResponse<Token> getToken(
			HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable String countryCode,
			@PathVariable String partyId,
			@PathVariable String tokenUid,
			@RequestParam(required = false, defaultValue = "RFID") TokenType type) {
		
		logger.info("countryCode = {}, partyId = {}, tokenUid = {}, type = {}", countryCode, partyId, tokenUid);
		
		// TODO: Validate params
		
		if (type == null) {
			type = TokenType.RFID;
		}
		Token result = this.tokenService.findToken(countryCode, partyId, tokenUid, type);
		if (result != null) {
			logger.info("Result = {}", result);
			response.setStatus(HttpStatus.OK.value());
			return new OcpiResponse<Token>(result, 1000, "Token found", new Date());
		}
		
		response.setStatus(HttpStatus.NOT_FOUND.value());
		return new OcpiResponse<Token>(null, 2000, "Token not found", new Date());
	}
	
	@PutMapping("/ocpi/2.2.1/tokens/{countryCode}/{partyId}/{tokenUid}")
	public OcpiResponse<Object> receiveToken(
			HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable String countryCode,
			@PathVariable String partyId,
			@PathVariable String tokenUid,
			@RequestParam(required = false, defaultValue = "RFID") TokenType type,
			@RequestBody(required = true) Token incomingToken) {
		
		logger.info("countryCode = {}, partyId = {}, tokenUid = {}, type = {}, incomingToken = {}", countryCode, partyId, tokenUid, type, incomingToken);
		
		// TODO: Validate params
		
		Token tokenToReplace = this.tokenService.findToken(countryCode, partyId, tokenUid, type);
		
		if (incomingToken.getLastUpdated() == null) {
			
			logger.info("last_updated field missing in incoming token", tokenToReplace);
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			return new OcpiResponse<Object>(null, 2001, "last_updated field missing", new Date());
		}
		else if (tokenToReplace != null) {
			
			if (incomingToken.getLastUpdated().getTime() < tokenToReplace.getLastUpdated().getTime()) {
				
				logger.warn("Incoming token is old than existing one ({} vs {}), no further update", incomingToken.getLastUpdated(), tokenToReplace.getLastUpdated());
				response.setStatus(HttpStatus.CONFLICT.value());
				return new OcpiResponse<Object>(null, 2000, "Incoming token is older than existing token in system", new Date());
			}
			else {
				
				this.tokenService.upsertToken(countryCode, partyId, tokenUid, type, incomingToken);
				response.setStatus(HttpStatus.OK.value());
				logger.info("Token replaced = {}", tokenToReplace);
				return new OcpiResponse<Object>(null, 1000, "Token replaced", new Date());
			}
		}
		else {
					
			this.tokenService.upsertToken(countryCode, partyId, tokenUid, type, incomingToken);
			response.setStatus(HttpStatus.CREATED.value());
			logger.info("Token created");
			return new OcpiResponse<Object>(null, 1000, "Token created", new Date());
		}
	}
	
	@PatchMapping("/ocpi/2.2.1/tokens/{countryCode}/{partyId}/{tokenUid}")
	public OcpiResponse<Object> updateToken(
			HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable String countryCode,
			@PathVariable String partyId,
			@PathVariable String tokenUid,
			@RequestParam(required = false, defaultValue = "RFID") TokenType type,
			@RequestBody(required = true) UpdateTokenDto payload) {
		
		logger.info("countryCode = {}, partyId = {}, tokenUid = {}, type = {}", countryCode, partyId, tokenUid, type);
		logger.info("Payload = {}", payload);
		Token token = this.tokenService.findToken(countryCode, partyId, tokenUid, type);
		
		if (token == null || token.getTokenType() != type) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return new OcpiResponse<Object>(null, 2000, "Token not found", new Date());
		}
		if (payload.last_updated == null) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			return new OcpiResponse<Object>(null, 2001, "last_updated field missing", new Date());
		}
		if (payload.last_updated.getTime() < token.getLastUpdated().getTime()) {
			logger.warn("Incoming token is old than existing one ({} vs {}), no further update", payload.last_updated, token.getLastUpdated());
			response.setStatus(HttpStatus.CONFLICT.value());
			return new OcpiResponse<Object>(null, 2000, "Incoming token is older than existing token in system", new Date());
		}
		
		boolean updated = false;
		
		if (payload.contract_id != null && !payload.contract_id.equals(token.getContractId())) {
			updated = true;
			token.setContractId(payload.contract_id);
		}
		if (payload.visual_number != null && !payload.visual_number.equals(token.getContractId())) {
			updated = true;
			token.setVisualNumber(payload.visual_number);
		}
		if (payload.issuer != null && !payload.issuer.equals(token.getIssuer())) {
			updated = true;
			token.setIssuer(payload.issuer);
		}
		if (payload.group_id != null && !payload.group_id.equals(token.getGroupId())) {
			updated = true;
			token.setGroupId(payload.group_id);
		}
		if (payload.valid != null && !payload.valid.equals(token.getValid())) {
			updated = true;
			token.setValid(payload.valid);
		}
		if (payload.whitelist != null && !payload.whitelist.equals(token.getWhitelist())) {
			updated = true;
			token.setWhitelist(payload.whitelist);
		}
		if (payload.language != null && !payload.language.equals(token.getLanguage())) {
			updated = true;
			token.setLanguage(payload.language);
		}
		if (payload.default_profile_type != null && !payload.default_profile_type.equals(token.getDefaultProfileType())) {
			updated = true;
			token.setDefaultProfileType(payload.default_profile_type);
		}
		if (payload.energy_contract != null && !payload.energy_contract.equals(token.getEnergyContract())) {
			updated = true;
			token.setEnergyContract(payload.energy_contract);
		}
		
		if (updated) {
			this.tokenService.upsertToken(countryCode, partyId, tokenUid, type, token);
		}
		else {
			logger.warn("Found token but no update was made");
		}
		
		response.setStatus(HttpStatus.OK.value());
		return new OcpiResponse<Object>(null, 1000, "Token updated", new Date());
	}
	
	/**
	 * The PATCH endpoint allows submission of partial token. Thus the token class does not work.
	 */
	private static class UpdateTokenDto {
		public String contract_id;
		public String visual_number;
		public String issuer;
		public String group_id;
		public Boolean valid;
		public WhitelistType whitelist;
		public LanguageCode language;
		public ProfileType default_profile_type;
		public EnergyContract energy_contract;
		public Date last_updated;
	}
}
