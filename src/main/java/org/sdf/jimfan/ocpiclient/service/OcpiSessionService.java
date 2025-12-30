package org.sdf.jimfan.ocpiclient.service;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Currency;
import java.util.Date;
import java.util.UUID;

import org.sdf.jimfan.ocpiclient.exception.OcpiException;
import org.sdf.jimfan.ocpiclient.model.datatype.AuthMethod;
import org.sdf.jimfan.ocpiclient.model.datatype.CountryCode;
import org.sdf.jimfan.ocpiclient.model.datatype.SessionStatus;
import org.sdf.jimfan.ocpiclient.model.location.Connector;
import org.sdf.jimfan.ocpiclient.model.location.EVSE;
import org.sdf.jimfan.ocpiclient.model.location.Location;
import org.sdf.jimfan.ocpiclient.model.session.Session;
import org.sdf.jimfan.ocpiclient.model.token.CdrToken;
import org.sdf.jimfan.ocpiclient.model.token.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

@Service
@Scope(value=WebApplicationContext.SCOPE_APPLICATION)
public class OcpiSessionService {

	@Autowired
	private OcpiConfigService configService;
	
	@Autowired
	private OcpiLocationService locationService;
	
	private final Logger logger = LoggerFactory.getLogger(OcpiSessionService.class);
	
	private static Session currentSession = null;
	
	public boolean hasActiveSession() {
		return currentSession != null;
	}
	
	/**
	 * Validate if given token can start charging. Throw exception if it is not.
	 */
	public void validateStartSessionRequest(Token tokenFromMSP, String chargeLocationId, String chargeStationUid, String connectorId, String responseUrl, String authorisationReference) throws OcpiException {
		
		// 1. Token, assuming that it has been validated by MSP, not checking it against tokens in this application, no other privilege checking
		if (tokenFromMSP == null) {
			throw new OcpiException(HttpStatus.BAD_REQUEST, null, "Token cannot be null");
		}
		
		Location theOneLocation = this.locationService.getLocation();
		EVSE theOneChargeLocation = theOneLocation.getEvses().getFirst();
		Connector theOneConnector = theOneChargeLocation.getConnectors().getFirst();
		
		// 2. Charge location ID, it must be that of the one location in this application
		if (chargeLocationId == null || chargeLocationId.length() <= 0) {
			throw new OcpiException(HttpStatus.BAD_REQUEST, null, "Charge location ID cannot be null / empty");
		}
		if (! chargeLocationId.equals(theOneLocation.getLocationId())) {
			throw new OcpiException(HttpStatus.BAD_REQUEST, null,
					String.format("Charge location ID %s does not exist", chargeLocationId));
		}
		
		// 3. Charge station UID is optional, when given it must be that of the one charger
		if (chargeStationUid != null && chargeStationUid.length() <= 0) {
			throw new OcpiException(HttpStatus.BAD_REQUEST, null, "Charge station UID cannot empty when present");
		}
		if (chargeStationUid != null && ! chargeStationUid.equals(theOneChargeLocation.getUid())) {
			throw new OcpiException(HttpStatus.BAD_REQUEST, null,
					String.format("Charge station UID %s does not exist", chargeStationUid));
		}
		
		// 4. Connector ID is optional, but must match that of the one charger if present
		if (connectorId != null && connectorId.length() <= 0) {
			throw new OcpiException(HttpStatus.BAD_REQUEST, null, "Connector ID cannot be empty when present");
		}
		if (connectorId != null && ! connectorId.equals(theOneConnector.getId())) {
			throw new OcpiException(HttpStatus.BAD_REQUEST, null,
					String.format("Connector ID %s does not exist", connectorId));
		}
		
		// 5. Response URL, mandatory
		if (responseUrl == null || responseUrl.isEmpty()) {
			throw new OcpiException(HttpStatus.BAD_REQUEST, null, "Response URL cannot be null / empty");
		}
		try {
			new URL(responseUrl).toURI();
		}
		catch (MalformedURLException | URISyntaxException e) {
			throw new OcpiException(HttpStatus.BAD_REQUEST, null, "Malformed response URL " + responseUrl);
		}
		
		// TODO: There should be more validation e.g. URL should have same base domain of sender
		return;
	}
	
	@Async
	public void startNewChargingSessionAsync(Token tokenStartingSession, Location chargeLocation, EVSE chargeStation, Connector connector) throws OcpiException {
	
		if (currentSession != null) {
			throw new OcpiException(HttpStatusCode.valueOf(200), null, "Active session exists (this implementation supports only one active session)");
		}
		
		CountryCode countryCode = CountryCode.valueOf(this.configService.getMyOcpiCountryCode());
		String partyId = this.configService.getMyOcpiPartyId();
		String sessionId = UUID.randomUUID().toString();
		Date sessionStartDatetime = new Date();
		Date sessionLastUpdateDatetime = sessionStartDatetime;
		Float energyConsumed = 0f;
		Currency currency = Currency.getInstance("GBP");
		SessionStatus sessionStatus = SessionStatus.PENDING;
	}
}
