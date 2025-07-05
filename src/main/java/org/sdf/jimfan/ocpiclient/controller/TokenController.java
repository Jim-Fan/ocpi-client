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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
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
	public OcpiResponse getToken(
			@PathVariable String countryCode,
			@PathVariable String partyId,
			@PathVariable String tokenUid,
			@RequestParam(required = false, defaultValue = "RFID") TokenType type) {
		
		logger.info("countryCode = {}, partyId = {}, tokenUid = {}, type = {}", countryCode, partyId, tokenUid);
		
		if (type == null) {
			type = TokenType.RFID;
		}
		Token result = this.tokenService.findToken(countryCode, partyId, tokenUid, type);
		if (result != null) {
			logger.info("Result = {}", result);
			return new OcpiResponse(result, 1000, "Token found", new Date());
		}
		return new OcpiResponse(null, 2000, "Token not found", new Date());
	}
	
	@PutMapping("/ocpi/2.2.1/tokens/{countryCode}/{partyId}/{tokenUid}")
	public OcpiResponse receiveToken(
			@PathVariable String countryCode,
			@PathVariable String partyId,
			@PathVariable String tokenUid,
			@RequestParam(required = false, defaultValue = "RFID") TokenType type,
			@RequestBody(required = true) Token incomingToken) {
		
		logger.info("countryCode = {}, partyId = {}, tokenUid = {}, type = {}", countryCode, partyId, tokenUid, type);
		logger.info("Incoming token = {}", incomingToken.toString());
		
		Token replacedToken = this.tokenService.upsertToken(countryCode, partyId, tokenUid, type, incomingToken);
		
		if (replacedToken != null) {
			logger.info("Token replaced = {}", replacedToken);
		}
		else {
			logger.info("Token inserted");
		}
		
		return new OcpiResponse(null, 1000, (replacedToken != null ? "Token replaced" : "Token inserted"), new Date());
	}
	
	@PatchMapping("/ocpi/2.2.1/tokens/{countryCode}/{partyId}/{tokenUid}")
	public OcpiResponse updateToken(
			@PathVariable String countryCode,
			@PathVariable String partyId,
			@PathVariable String tokenUid,
			@RequestParam(required = false, defaultValue = "RFID") TokenType type,
			@RequestBody(required = true) UpdateTokenDto payload) {
		
		logger.info("countryCode = {}, partyId = {}, tokenUid = {}, type = {}", countryCode, partyId, tokenUid, type);
		logger.info("Payload = {}", payload);
		Token token = this.tokenService.findToken(countryCode, partyId, tokenUid, type);
		
		if (token == null || token.getTokenType() != type) {
			return new OcpiResponse(null, 2000, "Token not found", new Date());
		}
		
		boolean updated = false;
		if (payload.contract_id != null) {
			updated = true;
			token.setContractId(payload.contract_id);
		}
		if (payload.visual_number != null) {
			updated = true;
			token.setVisualNumber(payload.visual_number);
		}
		if (payload.visual_number != null) {
			updated = true;
			token.setVisualNumber(payload.visual_number);
		}
		if (payload.issuer != null) {
			updated = true;
			token.setIssuer(payload.issuer);
		}
		if (payload.group_id != null) {
			updated = true;
			token.setGroup_id(payload.group_id);
		}
		if (payload.valid != null) {
			updated = true;
			token.setValid(payload.valid);
		}
		if (payload.whitelist != null) {
			updated = true;
			token.setWhitelist(payload.whitelist);
		}
		if (payload.language != null) {
			updated = true;
			token.setLanguage(payload.language);
		}
		if (payload.default_profile_type != null) {
			updated = true;
			token.setDefaultProfileType(payload.default_profile_type);
		}
		if (payload.energy_contract != null) {
			updated = true;
			token.setEnergyContract(payload.energy_contract);
		}
		if (payload.last_updated != null) {
			updated = true;
			token.setLastUpdated(payload.last_updated);
		}
		
		if (updated) {
			this.tokenService.upsertToken(countryCode, partyId, tokenUid, type, token);
		}
		else {
			logger.warn("Found token but no field updated");
		}
		
		if (updated) {
			return new OcpiResponse(null, 1000, "Token updated", new Date());
		}
		return new OcpiResponse(null, 1001, "Token found, but no update was made", new Date());
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
