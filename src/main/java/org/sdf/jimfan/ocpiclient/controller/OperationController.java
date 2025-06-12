package org.sdf.jimfan.ocpiclient.controller;

import org.sdf.jimfan.ocpiclient.model.credentials.BusinessDetails;
import org.sdf.jimfan.ocpiclient.model.credentials.Credentials;
import org.sdf.jimfan.ocpiclient.model.credentials.CredentialsRole;
import org.sdf.jimfan.ocpiclient.model.credentials.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;

/**
 * Interface for triggering operations by the OCPI client e.g. perform handshake, update config.
 */
@RestController
@EnableAutoConfiguration
@ComponentScan
@Scope(value=WebApplicationContext.SCOPE_APPLICATION)
public class OperationController {

	static final String ocpiServerCredentialsUrl = System.getenv("OCPI_SERVER_CREDENTIALS_URL");
	static final String ocpiHandshakeTokenA = System.getenv("OCPI_TOKEN_A");
	static final String ocpiHandshakeTokenB = System.getenv("OCPI_TOKEN_B");
	static final String ocpiPartyId = System.getenv("OCPI_PARTY_ID");
	static final String ocpiCountryCode = System.getenv("OCPI_COUNTRY_CODE");
	
	static final String thisApplicationProtocol = System.getenv("APP_HTTP_PROTOCOL");
	static final String thisApplicationDomain = System.getenv("APP_DOMAIN_NAME");
	
	static final Logger logger = LoggerFactory.getLogger(OperationController.class);
	
	@GetMapping("/op/handshake")
	public String performHandshake() {
		
		// Invoke /credentials endpoint against OCPI server, should receive token C in the end
		try {
			
			String encodedToken = new String(Base64.getEncoder().encode(ocpiHandshakeTokenA.getBytes()));
			logger.info("Handshake details: {}, {}, {}, {}",
				ocpiServerCredentialsUrl,
				ocpiHandshakeTokenA,
				ocpiHandshakeTokenB, encodedToken);

			Credentials postBody = new Credentials(
				ocpiHandshakeTokenB,
				thisApplicationProtocol + "://" + thisApplicationDomain + "/ocpi/versions",
				new CredentialsRole[] {
					new CredentialsRole(Role.CPO, new BusinessDetails("Jim's sample OCPI client", null, null),
						ocpiPartyId, ocpiCountryCode) });

			ObjectMapper objectMapper = new ObjectMapper();
			logger.info("Body = {}", objectMapper.writeValueAsString(postBody));
			
			RestClient restClient = RestClient.create();
			ResponseEntity<String> response = restClient
				.post()
				.uri(ocpiServerCredentialsUrl)
				.body(postBody)
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization",
						"Token " + new String(Base64.getEncoder().encode(ocpiHandshakeTokenA.getBytes())))	// required by OCPI 2.2.1
				.retrieve()
				.toEntity(String.class);
				//.toBodilessEntity();
			
			logger.info("Response = {}", response.getBody());
			return response.getBody();
		}
		catch (Exception ex) {
			return ex.getMessage();
		}
	}
}
