package org.sdf.jimfan.ocpiclient.controller;

import org.sdf.jimfan.ocpiclient.model.OcpiResponse;
import org.sdf.jimfan.ocpiclient.model.tariff.*;
import org.sdf.jimfan.ocpiclient.service.OcpiLocationService;
import org.sdf.jimfan.ocpiclient.service.OcpiTariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Implementation closely follows that of <code>LocationController</code>.
 */
@RestController
@EnableAutoConfiguration
@ComponentScan
@Scope(value=WebApplicationContext.SCOPE_APPLICATION)
public class TariffController {

	private final Logger logger = LoggerFactory.getLogger(TariffController.class);
	
	@Autowired
	private OcpiTariffService tariffService;
	
	@GetMapping("/ocpi/2.2.1/tariffs")
	public OcpiResponse<List<Tariff>> getTariffs(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(name = "date_from", required = false) Date dateFrom,
			@RequestParam(name = "date_to", required = false) Date dateTo,
			@RequestParam(name = "offset", required = false) Integer offset,
			@RequestParam(name = "limit", required = false) Integer limit) {
		
		List<Tariff> tariffs = List.of(this.tariffService.getTariff());
		OcpiResponse<List<Tariff>> ocpiResponse = new OcpiResponse<List<Tariff>>(tariffs, 1000, "OK", new Date());
		
		// Potential for code re-use
		String requestId = request.getHeader("X-Request-ID");
		String correlationId = request.getHeader("X-Correlation-ID");		
		if (requestId == null) {
			logger.warn("Null X-Request-ID, assigning random ID in response");
			response.setHeader("X-Request-ID", UUID.randomUUID().toString());
		}
		else {
			response.setHeader("X-Request-ID", requestId);
		}
		if (requestId == null) {
			logger.warn("Null X-Correlation-ID, assigning random ID in response");
			response.setHeader("X-Correlation-ID", UUID.randomUUID().toString());
		}
		else {
			response.setHeader("X-Correlation-ID", correlationId);
		}
		response.setHeader("X-Total-Count", String.valueOf(tariffs.size()));
		response.setHeader("X-Limit", String.valueOf(tariffs.size()));
		response.setStatus(HttpStatus.OK.value());
		
		return ocpiResponse;
	}
}
