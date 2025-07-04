package org.sdf.jimfan.ocpiclient.controller;

import org.sdf.jimfan.ocpiclient.model.OcpiResponse;
import org.sdf.jimfan.ocpiclient.model.credential.BusinessDetails;
import org.sdf.jimfan.ocpiclient.model.credential.Credentials;
import org.sdf.jimfan.ocpiclient.model.credential.CredentialsRole;
import org.sdf.jimfan.ocpiclient.model.credential.Role;
import org.sdf.jimfan.ocpiclient.service.OcpiConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;


@RestController
@EnableAutoConfiguration
@ComponentScan
@Scope(value=WebApplicationContext.SCOPE_APPLICATION)
public class CredentialController {
	
	private final Logger logger = LoggerFactory.getLogger(CredentialController.class);
	
	@Autowired
	private OcpiConfigService ocpiConfigService;

	@PutMapping({"/ocpi/2.2.1/credentials", "/ocpi/2.2.1/credentials/"})
	public OcpiResponse receiveHandshake(@RequestBody Credentials credentials) {
		
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			logger.info("Credential received = {}", objectMapper.writeValueAsString(credentials));
			
			// TODO: Check handshake token
			String newTokenC = credentials.getToken();
			this.ocpiConfigService.setMyOcpiCredentialToken(newTokenC);
			logger.info("Updated my credential token = {}", newTokenC);
			
			// Should return new credential to caller
			// TODO: Make this a function
			// TODO: Should invoke /versions and version details endpoint of server
			Credentials newTheirCredential = new Credentials(
				this.ocpiConfigService.getTheirOcpiCredentialToken(),
				this.ocpiConfigService.getMyApplicationProtocol() + "://" + this.ocpiConfigService.getMyApplicationDomain() + "/ocpi/versions",
				new CredentialsRole[] {
					new CredentialsRole(
						Role.CPO,
						new BusinessDetails("Jim's sample OCPI client", null, null),
						this.ocpiConfigService.getMyOcpiPartyId(),
						this.ocpiConfigService.getMyOcpiCountryCode()) 
					});
			
			return new OcpiResponse(newTheirCredential, 1000, "OK", new Date());
		}
		catch (Exception ex) {
			logger.error(ex.getMessage());
			ex.printStackTrace();
			return new OcpiResponse(null, 2000, "Unexpected error", new Date());
		}
	}
}
