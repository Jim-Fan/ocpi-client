package org.sdf.jimfan.ocpiclient.service;

import java.util.Base64;

import org.sdf.jimfan.ocpiclient.exception.OcpiException;
import org.sdf.jimfan.ocpiclient.model.OcpiResponse;
import org.sdf.jimfan.ocpiclient.model.credential.BusinessDetails;
import org.sdf.jimfan.ocpiclient.model.credential.Credentials;
import org.sdf.jimfan.ocpiclient.model.credential.CredentialsRole;
import org.sdf.jimfan.ocpiclient.model.credential.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.context.WebApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Scope(value=WebApplicationContext.SCOPE_APPLICATION)
public class OcpiCredentialService {
	
	private final Logger logger = LoggerFactory.getLogger(OcpiCredentialService.class);

	@Autowired
	private OcpiConfigService configService;
	
	//TODO: Should throw exception when error occurred
	public String performHandshake() {
		
		String tokenA = this.configService.getMyOcpiHandshakeToken();
		String tokenB = this.configService.getTheirOcpiCredentialToken();
		String serverCredentialUrl = this.configService.getTheirOcpiCredentialsUrl();
		String protocol = this.configService.getMyApplicationProtocol();
		String domain = this.configService.getMyApplicationDomain();
		String partyId = this.configService.getMyOcpiPartyId();
		String countryCode = this.configService.getMyOcpiCountryCode();
		
		ResponseEntity<OcpiResponse> response = null;
		ObjectMapper objectMapper = new ObjectMapper();
		
		// Invoke /credentials endpoint against OCPI server, should receive token C in the end
		try {
			
			String encodedToken = new String(Base64.getEncoder().encode(tokenA.getBytes()));
			logger.info("Handshake details: {}, {}, {}, {}",
				serverCredentialUrl,
				tokenA,
				encodedToken,
				tokenB);

			Credentials postBody = new Credentials(
				tokenB,
				protocol + "://" + domain + "/ocpi/versions",
				new CredentialsRole[] {
					new CredentialsRole(
						Role.CPO,
						new BusinessDetails("Jim's sample OCPI client", null, null),
						partyId,
						countryCode) 
					});
			
			logger.info("Body = {}", objectMapper.writeValueAsString(postBody));
			
			RestClient restClient = RestClient.create();
			response = restClient
				.post()
				.uri(serverCredentialUrl)
				.body(postBody)
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Token " + encodedToken)    // required by OCPI 2.2.1
				.retrieve()
				.toEntity(OcpiResponse.class);
				//.toBodilessEntity();
			
			logger.info("Response = {}", response.getBody());
			return objectMapper.writeValueAsString(response);
		}
		catch (RestClientResponseException restEx) {
			
			// This happens when server returns HTTP status >= 400. OCPI protocol guarantees a
			// textual response even when error occurs so reading response body makes sense.
			logger.error(restEx.getResponseBodyAsString());
			return restEx.getResponseBodyAsString();
		}
		catch (Exception ex) {
			logger.error(ex.getMessage());
			ex.printStackTrace();
			return ex.getMessage();
		}
	}
}
