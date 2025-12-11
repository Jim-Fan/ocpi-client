package org.sdf.jimfan.ocpiclient.service;

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
import org.springframework.http.HttpStatusCode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

@Service
@Scope(value=WebApplicationContext.SCOPE_APPLICATION)
public class OcpiSessionService {

	@Autowired
	private OcpiConfigService configService;
	
	private final Logger logger = LoggerFactory.getLogger(OcpiSessionService.class);
	
	private static Session currentSession = null;
	
	public boolean hasActiveSession() {
		return currentSession != null;
	}
	
	@Async
	public synchronized void startNewChargingSessionAsync(Token tokenStartingSession, Location chargeLocation, EVSE chargeStation, Connector connector) throws OcpiException {
	
		/* // To be implemented
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
		*/
	}
}
