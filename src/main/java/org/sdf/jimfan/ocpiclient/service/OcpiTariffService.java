package org.sdf.jimfan.ocpiclient.service;

import org.sdf.jimfan.ocpiclient.model.OcpiResponse;
import org.sdf.jimfan.ocpiclient.model.datatype.*;
import org.sdf.jimfan.ocpiclient.model.tariff.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.context.WebApplicationContext;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.util.Currency;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Implementation closely follows that of <code>OcpiLocationService</code>.
 */
@Service
@Scope(value=WebApplicationContext.SCOPE_APPLICATION)
public class OcpiTariffService {

	private final Logger logger = LoggerFactory.getLogger(OcpiTariffService.class);
	
	@Autowired
	private OcpiConfigService configService;
	
	private Tariff theOneTariff;
	
	@PostConstruct
	private void initialiseTariff() {
		
		if (this.theOneTariff != null) return;
		
		String countryCode = this.configService.getMyOcpiCountryCode();
		String partyId = this.configService.getMyOcpiPartyId();
		TimeZone UTC = TimeZone.getTimeZone("UTC");
		ZonedDateTime lastUpdateDate = ZonedDateTime.now();
		
		PriceComponent energyComponent = new PriceComponent(TariffDimensionType.ENERGY, 0.5F, 1);
		energyComponent.setVat(20.0F);
		List<TariffElement> elements = List.of(new TariffElement(List.of(energyComponent)));
		
		Tariff tariff = new Tariff(CountryCode.valueOf(countryCode), partyId, "520726f1-a965-4b27-9fe6-d866a57c06ba",
				Currency.getInstance("GBP"),
				elements,
				lastUpdateDate);
		
		this.theOneTariff = tariff;
		logger.info("Tariff service initialised");
	}
	
	@Async
	public void putTariffToEMSPAsync() {
		this.putTariffToEMSP();
	}
	
	public void putTariffToEMSP() {

		String serverCredentialUrl = this.configService.getTheirOcpiCredentialsUrl();
		String partyId = this.configService.getMyOcpiPartyId();
		String countryCode = this.configService.getMyOcpiCountryCode();
		String encodedToken = this.configService.getMyOcpiCredentialTokenEncoded();
		
		ResponseEntity<OcpiResponse<Object>> httpResponse = null;
		RestClient restClient = RestClient.create();
		
		try {
			
			// TODO: Get URL from server instead
			String url = String.format("%s/%s/%s/%s",
					serverCredentialUrl.replace("/credentials", "/tariffs"),
					this.theOneTariff.getCountryCode().toString(),
					this.theOneTariff.getPartyId(),
					this.theOneTariff.getId());
			logger.info("Put tariff url = {}", url);
			logger.info("Put tariff = {}", this.theOneTariff);
			
			String requestId = UUID.randomUUID().toString();
			String correlationId = UUID.randomUUID().toString();
			
			httpResponse = restClient
					.put()
					.uri(url)
					.body(this.theOneTariff)
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
				logger.info("Put tariff response = {}", ocpiResponse);
			}
			else {
				logger.warn("Put tariff response (abnormal) = {}", ocpiResponse);
			}
		}
		catch (Exception ex) {
			logger.error("Error when sending tariffs to server, abort");
			logger.error(ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	public Tariff getTariff() {
		return this.theOneTariff;
	}
}
