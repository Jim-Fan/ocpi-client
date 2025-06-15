package org.sdf.jimfan.ocpiclient.service;

import java.util.List;

import org.sdf.jimfan.ocpiclient.model.OcpiResponse;
import org.sdf.jimfan.ocpiclient.model.datatypes.CountryCode;
import org.sdf.jimfan.ocpiclient.model.datatypes.GeoLocation;
import org.sdf.jimfan.ocpiclient.model.locations.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.ZoneId;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

@Service
@Scope(value=WebApplicationContext.SCOPE_APPLICATION)
public class OcpiLocationService {

	private final Logger logger = LoggerFactory.getLogger(OcpiLocationService.class);
	
	@Autowired
	private OcpiConfigService configService;
	
	private List<Location> locations;
	
	private void initialiseLocations() {
		String countryCode = this.configService.getMyOcpiCountryCode();
		String partyId = this.configService.getMyOcpiPartyId();
		TimeZone UTC = TimeZone.getTimeZone("UTC");
		Date lastUpdateDate = new Date();
		
		this.locations = Collections.synchronizedList(List.of(
				new Location(countryCode, partyId, String.format("%s-%s-%s", countryCode, partyId, "LOC1"), true, "Address of location 1", "Reading", CountryCode.GBR, new GeoLocation("51.456806", "-0.955806"), UTC, lastUpdateDate),
				new Location(countryCode, partyId, String.format("%s-%s-%s", countryCode, partyId, "LOC2"), true, "Address of location 2", "Basingstoke", CountryCode.GBR, new GeoLocation("51.266806", "-1.086557"), UTC, lastUpdateDate),
				new Location(countryCode, partyId, String.format("%s-%s-%s", countryCode, partyId, "LOC3"), true, "Address of location 3", "Oxford", CountryCode.GBR, new GeoLocation("51.753536", "-1.258603"), UTC, lastUpdateDate)
			));
	}
	
	public void pushLocationToServer() {
		
		if (this.locations == null) {
			this.initialiseLocations();
		}
		String tokenA = this.configService.getMyOcpiHandshakeToken();
		String tokenB = this.configService.getTheirOcpiCredentialToken();
		String tokenC = this.configService.getMyOcpiCredentialToken();
		String serverCredentialUrl = this.configService.getTheirOcpiCredentialsUrl();
		String protocol = this.configService.getMyApplicationProtocol();
		String domain = this.configService.getMyApplicationDomain();
		String partyId = this.configService.getMyOcpiPartyId();
		String countryCode = this.configService.getMyOcpiCountryCode();
		
		String encodedToken = new String(Base64.getEncoder().encode(tokenC.getBytes()));
		
		ResponseEntity<OcpiResponse> response = null;
		ObjectMapper objectMapper = new ObjectMapper();
		RestClient restClient = RestClient.create();
		
		try {
			// This locks this.locations for very long time due to network operations
			// TODO: Fire location push event to an asynchronous event queue
			synchronized (this.locations) {
				
				Iterator<Location> iter = this.locations.iterator();
				while (iter.hasNext()) {
			
					Location location = iter.next();
					
					// TODO: Get location URL from server instead
					String url = String.format("%s/%s/%s/%s",
							serverCredentialUrl.replace("/credentials", "/locations"),
							countryCode,
							partyId,
							location.getLocationId());
					logger.info("Put location url = " + url);
					logger.info("Put location = " + objectMapper.writeValueAsString(location));
					
					response = restClient
							.put()
							.uri(url)
							.body(location)
							.contentType(MediaType.APPLICATION_JSON)
							.header("Authorization", "Token " + encodedToken)    // required by OCPI 2.2.1
							.retrieve()
							.toEntity(OcpiResponse.class);
					
					logger.info("Put location response = " + objectMapper.writeValueAsString(response.getBody()));
				}
				
				logger.info("Location push completed");
			}
		}
		catch (Exception ex) {
			logger.error("Error when sending locations to server, abort");
			logger.error(ex.getMessage());
			ex.printStackTrace();
		}
	}
}
