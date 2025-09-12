package org.sdf.jimfan.ocpiclient.controller;

import org.sdf.jimfan.ocpiclient.model.OcpiResponse;
import org.sdf.jimfan.ocpiclient.model.datatype.AuthorizationInfo;
import org.sdf.jimfan.ocpiclient.model.datatype.TokenType;
import org.sdf.jimfan.ocpiclient.service.OcpiCredentialService;
import org.sdf.jimfan.ocpiclient.service.OcpiLocationService;
import org.sdf.jimfan.ocpiclient.service.OcpiTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interface for triggering operations by the OCPI client e.g. perform handshake, update config.
 */
@RestController
@EnableAutoConfiguration
@ComponentScan
@Scope(value=WebApplicationContext.SCOPE_APPLICATION)
public class OperationController {

	static final Logger logger = LoggerFactory.getLogger(OperationController.class);
	
	@Autowired
	private OcpiCredentialService credentialService;
	
	@Autowired
	private OcpiLocationService locationService;
	
	@Autowired
	private OcpiTokenService tokenService;
	
	@GetMapping("/op/handshake")
	public String performHandshake() {
		
		try {
			String response = this.credentialService.performHandshake();
			return response;
		}
		catch (Exception ex) {
			return ex.getMessage();
		}
	}
	
	@GetMapping("/op/push-location")
	public String pushLocation() {
		
		try {
			this.locationService.pushLocationToServer();
			return "OK";
		}
		catch (Exception ex) {
			return ex.getMessage();
		}
	}
	
	@GetMapping("/op/pull-token")
	public String pullTokensFromServer() {
		
		try {
			this.tokenService.pullTokensFromServer();
			return "OK";
		}
		catch (Exception ex) {
			return ex.getMessage();
		}
	}
	
	@GetMapping(value="/op/try-authorise-token", produces="application/json")
	public String authoriseToken(
			@RequestParam(required=false, defaultValue="RFID") String tokenType,
			@RequestParam(required=true) String uid) {
		
		try {
			OcpiResponse<AuthorizationInfo> authResponse = this.tokenService.tryAuthoriseToken(tokenType, uid);
			return authResponse.toString();
		}
		catch (Exception ex) {
			return ex.getMessage();
		}
	}
}
