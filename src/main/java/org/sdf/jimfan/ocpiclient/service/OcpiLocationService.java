package org.sdf.jimfan.ocpiclient.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.context.WebApplicationContext;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;


/**
 * OCPI location service. Support operation of location controller and may also
 * provide operations to invoke receiver interface (i.e. EMSP) by OCPI 2.2.1-d2
 * section 8.2.2.
 * 
 * The receiver interface provides these endpoints but currently they are not
 * called by this service:
 *
 *    GET /ocpi/2.2.1/locations/{locationId}
 *    GET /ocpi/2.2.1/locations/{locationId}/{evseUid}
 *    GET /ocpi/2.2.1/locations/{locationId}/{evseUid}/{connectorId}
 *    
 * By requirement of OCPI protocol, the service should:
 * 
 *     Set correct API token in outbound call
 *     
 *     Set X-Request-ID and X-Correlation-ID in outbound HTTP headers
 *     
 *     Check HTTP status and OCPI status code in API response
 */
@Service
@Scope(value=WebApplicationContext.SCOPE_APPLICATION)
public class OcpiLocationService {

	private final Logger logger = LoggerFactory.getLogger(OcpiLocationService.class);
	
	@Autowired
	private OcpiConfigService configService;
	
	@Autowired
	private OcpiTariffService tariffService;
	
	private Location theOneLocation;
	
	@PostConstruct
	private void initialiseLocation() {
		
		if (this.theOneLocation != null) return;
		
		String countryCode = this.configService.getMyOcpiCountryCode();
		String partyId = this.configService.getMyOcpiPartyId();
		TimeZone UTC = TimeZone.getTimeZone("UTC");
		Date lastUpdateDate = new Date();
		
		Location reading = new Location(
				countryCode,
				partyId,
				String.format("%s-%s-%s", countryCode, partyId, "LOC1"),
				true,
				"Reading - Forbury Retail Park",
				"Reading", CountryCode.GB,
				new GeoLocation("51.457641", "-0.963232"),
				UTC,
				lastUpdateDate);
		reading.setName("Forbury Retail Park");
		reading.setPostalCode("RG1 3HS");
		
		Connector connector = new Connector("512f421f-351a-45a6-bf26-98661b55a7e8",
				ConnectorType.IEC_62196_T3C,
				ConnectorFormat.CABLE,
				PowerType.DC,
				500,
				120,
				lastUpdateDate);
		connector.setMaxElectricPower(connector.getMaxAmperage() * connector.getMaxVoltage());
		connector.setTariffIds(List.of(this.tariffService.getTariff().getId()));
		
		EVSE readingCharger = new EVSE(
				"c410f411-ee01-4e33-a0ad-4d86f678bfd1",  // uid
				String.format("%s*%s*E*%s", countryCode, partyId, "46709394"), // EVSE ID
				Status.PLANNED,
				List.of(connector),
				"FORBURY-1", // physical reference
				lastUpdateDate);
		readingCharger.setCapabilities(List.of(Capability.REMOTE_START_STOP_CAPABLE));
		reading.setEvses(List.of(readingCharger));
		
		this.theOneLocation = reading;
		logger.info("Location initialised");
	}
	
	public void putLocationToEMSP() {

		String serverCredentialUrl = this.configService.getTheirOcpiCredentialsUrl();
		String partyId = this.configService.getMyOcpiPartyId();
		String countryCode = this.configService.getMyOcpiCountryCode();
		String encodedToken = this.configService.getMyOcpiCredentialTokenEncoded();
		
		ResponseEntity<OcpiResponse<Object>> httpResponse = null;
		RestClient restClient = RestClient.create();

		try {
			
			// TODO: Get location URL from server instead
			String url = String.format("%s/%s/%s/%s",
					serverCredentialUrl.replace("/credentials", "/locations"),
					countryCode,
					partyId,
					this.theOneLocation.getLocationId());
			logger.info("Put location url = {}", url);
			logger.info("Put location = {}", this.theOneLocation);
		
			String requestId = UUID.randomUUID().toString();
			String correlationId = UUID.randomUUID().toString();
			
			httpResponse = restClient
					.put()
					.uri(url)
					.body(this.theOneLocation)
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Token " + encodedToken)    // required by OCPI 2.2.1
					.header("X-Request-ID", requestId)
					.header("X-Correlation-ID", correlationId)
					.retrieve()
					.toEntity(new ParameterizedTypeReference<>() {});
			
			// TODO: This logic must repeat across this and other service class. Re-use it.
			if (httpResponse.getStatusCode() != HttpStatus.OK) {
				logger.warn("Server responded with HTTP status {}", httpResponse.getStatusCode());
			}
			
			boolean foundRequestId = false;
			for (String s: httpResponse.getHeaders().get("X-Request-ID")) {
				if (s.equals(requestId)) {
					foundRequestId = true;
					break;
				}
			}
			if (! foundRequestId) {
				logger.warn("Cannot locate same X-Request-ID in response");
			}
			
			boolean foundCorrelationId = false;
			for (String s: httpResponse.getHeaders().get("X-Correlation-ID")) {
				if (s.equals(correlationId)) {
					foundCorrelationId = true;
					break;
				}
			}
			if (! foundCorrelationId) {
				logger.warn("Cannot locate same X-Correlation-ID in response");
			}
			
			OcpiResponse<Object> ocpiResponse = httpResponse.getBody();
			if (ocpiResponse.getStatusCode() == 1000) {
				logger.info("Put location response = {}", ocpiResponse);
			}
			else {
				logger.warn("Put location response (abnormal) = {}", ocpiResponse);
			}
		}
		catch (Exception ex) {
			logger.error("Error when sending locations to server, abort");
			logger.error(ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	public Location getLocation() {
		return this.theOneLocation;
	}
}
