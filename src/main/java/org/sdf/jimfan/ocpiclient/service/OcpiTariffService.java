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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Implementation closely follows that of <code>OcpiLocationService</code>.
 */
@Service("OcpiTariffService")
@Scope(value=WebApplicationContext.SCOPE_APPLICATION)
public class OcpiTariffService {

	private final Logger logger = LoggerFactory.getLogger(OcpiTariffService.class);
	
	@Autowired
	private OcpiConfigService configService;
	
	private Map<String, Tariff> tariffRepository;
	
	@PostConstruct
	private void initialiseTariff() {
		
		if (this.tariffRepository != null) return;
		this.tariffRepository = new HashMap<String, Tariff>();
		
		String countryCode = this.configService.getMyOcpiCountryCode();
		String partyId = this.configService.getMyOcpiPartyId();
		TimeZone UTC = TimeZone.getTimeZone("UTC");
		ZonedDateTime lastUpdateDate = ZonedDateTime.now();
		
		// See "Free of Charge Tariff example" in OCPI 2.2.1-d2
		PriceComponent freeOfChargeComponent = new PriceComponent(TariffDimensionType.FLAT, 0.0F, 0);
		freeOfChargeComponent.setVat(20.0F);
		
		PriceComponent energyComponent = new PriceComponent(TariffDimensionType.ENERGY, 0.5F, 1);
		energyComponent.setVat(20.0F);
		
		PriceComponent flatComponent = new PriceComponent(TariffDimensionType.FLAT, 2.0F, 1);
		flatComponent.setVat(20.0F);
		
		PriceComponent parkingComponent = new PriceComponent(TariffDimensionType.PARKING_TIME, 1.0F, 300);
		parkingComponent.setVat(20.0F);
		
		// See "Free of Charge Tariff example" in OCPI 2.2.1-d2
		Tariff freeOfChargeTariff = new Tariff(CountryCode.valueOf(countryCode), partyId, "2d15bb72-5987-4626-ab51-5298da0a6a5c",
				Currency.getInstance("GBP"),
				List.of(new TariffElement(List.of(freeOfChargeComponent))),
				lastUpdateDate);
		
		Tariff energyOnlyTariff = new Tariff(CountryCode.valueOf(countryCode), partyId, "520726f1-a965-4b27-9fe6-d866a57c06ba",
				Currency.getInstance("GBP"),
				List.of(new TariffElement(List.of(energyComponent))),
				lastUpdateDate);
		
		Tariff energyAndFlatTariff = new Tariff(CountryCode.valueOf(countryCode), partyId, "65315493-d9e2-4a31-aad4-551f42cfe1ae",
				Currency.getInstance("GBP"),
				List.of(new TariffElement(List.of(energyComponent, flatComponent))),
				lastUpdateDate);
		
		Tariff energyAndParkingTariff = new Tariff(CountryCode.valueOf(countryCode), partyId, "7880418f-847c-4cd3-ac97-2df2404c986c",
				Currency.getInstance("GBP"),
				List.of(new TariffElement(List.of(energyComponent, parkingComponent))),
				lastUpdateDate);
		
		Tariff energyAndFlatAndParkingTariff = new Tariff(CountryCode.valueOf(countryCode), partyId, "854c939d-44f2-43c7-ab82-94b5bf382477",
				Currency.getInstance("GBP"),
				List.of(new TariffElement(List.of(energyComponent, flatComponent, parkingComponent))),
				lastUpdateDate);
		
		this.tariffRepository.put(freeOfChargeTariff.getId(), freeOfChargeTariff);
		this.tariffRepository.put(energyOnlyTariff.getId(), energyOnlyTariff);
		this.tariffRepository.put(energyAndFlatTariff.getId(), energyAndFlatTariff);
		this.tariffRepository.put(energyAndParkingTariff.getId(), energyAndParkingTariff);
		this.tariffRepository.put(energyAndFlatAndParkingTariff.getId(), energyAndFlatAndParkingTariff);
		
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
			for (Tariff tariffToPush: this.tariffRepository.values()) {

				// TODO: Get URL from server instead
				String url = String.format("%s/%s/%s/%s",
						serverCredentialUrl.replace("/credentials", "/tariffs"),
						tariffToPush.getCountryCode().toString(),
						tariffToPush.getPartyId(),
						tariffToPush.getId());
				logger.info("Put tariff url = {}", url);
				logger.info("Put tariff = {}", tariffToPush);
				
				String requestId = UUID.randomUUID().toString();
				String correlationId = UUID.randomUUID().toString();
				
				httpResponse = restClient
						.put()
						.uri(url)
						.body(tariffToPush)
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
					logger.warn("Put tariff response code = {}, response = {}", ocpiResponse.getStatusCode(), ocpiResponse);
				}	
			}
		}
		catch (Exception ex) {
			logger.error("Error when sending tariffs to server, abort");
			logger.error(ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	public Tariff getTariffById(String tariffId) {
		if (this.tariffRepository == null) {
			throw new RuntimeException("Tariff service is not yet initialised");
		}
		return this.tariffRepository.get(tariffId);
	}
	
	public List<Tariff> getAllTariffs() {
		if (this.tariffRepository == null) {
			throw new RuntimeException("Tariff service is not yet initialised");
		}
		
		// copyOf() is deep copy, which is probably too much but I will live with it
		return List.copyOf(this.tariffRepository.values());
	}
}
