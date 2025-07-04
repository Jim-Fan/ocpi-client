package org.sdf.jimfan.ocpiclient.service;

import java.util.List;

import org.sdf.jimfan.ocpiclient.model.OcpiResponse;
import org.sdf.jimfan.ocpiclient.model.datatype.Capability;
import org.sdf.jimfan.ocpiclient.model.datatype.ConnectorFormat;
import org.sdf.jimfan.ocpiclient.model.datatype.ConnectorType;
import org.sdf.jimfan.ocpiclient.model.datatype.CountryCode;
import org.sdf.jimfan.ocpiclient.model.datatype.GeoLocation;
import org.sdf.jimfan.ocpiclient.model.datatype.PowerType;
import org.sdf.jimfan.ocpiclient.model.datatype.Status;
import org.sdf.jimfan.ocpiclient.model.location.Connector;
import org.sdf.jimfan.ocpiclient.model.location.EVSE;
import org.sdf.jimfan.ocpiclient.model.location.Location;
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
		
		Location reading = new Location(countryCode, partyId, String.format("%s-%s-%s", countryCode, partyId, "LOC1"), true, "Address of location 1", "Reading", CountryCode.GB, new GeoLocation("51.456806", "-0.955806"), UTC, lastUpdateDate);
		reading.setName("Reading Somewhere");
		EVSE readingCharger = new EVSE(
				"c410f411-ee01-4e33-a0ad-4d86f678bfd1",  // uid
				String.format("%s*%s*E*%s", countryCode, partyId, "READING001"), // EVSE ID
				Status.PLANNED,
				List.of(new Connector("512f421f-351a-45a6-bf26-98661b55a7e8",
						ConnectorType.IEC_62196_T3C,
						ConnectorFormat.CABLE,
						PowerType.DC,
						500,
						120,
						lastUpdateDate)),
				"READING-001", // physical reference
				lastUpdateDate);
		readingCharger.setCapabilities(List.of(Capability.REMOTE_START_STOP_CAPABLE));
		reading.setEvses(List.of(readingCharger));
		
		Location basingstoke = new Location(countryCode, partyId, String.format("%s-%s-%s", countryCode, partyId, "LOC2"), true, "Address of location 2", "Basingstoke", CountryCode.GB, new GeoLocation("51.266806", "-1.086557"), UTC, lastUpdateDate);
		basingstoke.setName("Basingstoke Somewhere");
		EVSE basingstokeCharger = new EVSE(
				"914db198-4a24-42e0-9528-cc2c8a1e7aca",  // uid
				String.format("%s*%s*E*%s", countryCode, partyId, "BASINGSTOKE001"), // EVSE ID
				Status.PLANNED,
				List.of(new Connector("ffd491ae-9d48-4a6c-9af3-93fd8bcd991f",
						ConnectorType.IEC_62196_T3C,
						ConnectorFormat.CABLE,
						PowerType.DC,
						500,
						120,
						lastUpdateDate)),
				"BASINGSTOKE-001", // physical reference
				lastUpdateDate);
		basingstokeCharger.setCapabilities(List.of(Capability.REMOTE_START_STOP_CAPABLE));
		basingstoke.setEvses(List.of(basingstokeCharger));
		
		Location oxford = new Location(countryCode, partyId, String.format("%s-%s-%s", countryCode, partyId, "LOC3"), true, "Address of location 3", "Oxford", CountryCode.GB, new GeoLocation("51.753536", "-1.258603"), UTC, lastUpdateDate);
		oxford.setName("Oxford Somewhere");
		EVSE oxfordCharger = new EVSE(
				"bf4c6e76-e8fb-49c6-b50c-21dec8cd3fb3",  // uid
				String.format("%s*%s*E*%s", countryCode, partyId, "OXFORD001"), // EVSE ID
				Status.PLANNED,
				List.of(new Connector("18619a1e-6bfa-4207-b76a-e5d6cfc4612e",
						ConnectorType.IEC_62196_T3C,
						ConnectorFormat.CABLE,
						PowerType.DC,
						500,
						120,
						lastUpdateDate)),
				"OXFORD-001", // physical reference
				lastUpdateDate);
		oxfordCharger.setCapabilities(List.of(Capability.REMOTE_START_STOP_CAPABLE));
		oxford.setEvses(List.of(oxfordCharger));
		
		this.locations = Collections.synchronizedList(List.of(
				reading,
				basingstoke,
				oxford));
	}
	
	public void pushLocationToServer() {
		
		if (this.locations == null) {
			this.initialiseLocations();
		}
		String serverCredentialUrl = this.configService.getTheirOcpiCredentialsUrl();
		String partyId = this.configService.getMyOcpiPartyId();
		String countryCode = this.configService.getMyOcpiCountryCode();
		String encodedToken = this.configService.getMyOcpiCredentialTokenEncoded();
		
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
					
					OcpiResponse resp = response.getBody();
					if (resp.getStatusCode() == 1000) {
						logger.info("Put location response = " + objectMapper.writeValueAsString(resp));
					}
					else {
						logger.warn("(ERROR) Put location response = " + objectMapper.writeValueAsString(resp));
					}
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
