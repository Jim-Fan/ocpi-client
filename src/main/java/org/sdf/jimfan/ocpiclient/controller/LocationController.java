package org.sdf.jimfan.ocpiclient.controller;

import org.sdf.jimfan.ocpiclient.model.OcpiResponse;
import org.sdf.jimfan.ocpiclient.model.location.Location;
import org.sdf.jimfan.ocpiclient.service.OcpiLocationService;
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
 * OCPI location controller. Handle sender interface according to OCPI 2.2.1-d2 section 8.2.1.
 * 
 * By requirement of OCPI protocol, the controller should:
 * 
 *     Verify incoming API token
 *     
 *     Handle X-Request-ID and X-Correlation-ID in inbound HTTP header
 *     
 *     Return HTTP/404 when resource does not exist
 *     
 *     Return HTTP/200 when update has been made
 *     
 *     Return HTTP/201 when resource has been created
 *     
 *     Set OCPI response statuses, see section protocol 5.1-5.4
 */
@RestController
@EnableAutoConfiguration
@ComponentScan
@Scope(value=WebApplicationContext.SCOPE_APPLICATION)
public class LocationController {

	private final Logger logger = LoggerFactory.getLogger(LocationController.class);
	
	@Autowired
	private OcpiLocationService locationService;
	
	@GetMapping("/ocpi/2.2.1/locations")
	public OcpiResponse<List<Location>> getLocations(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(name = "date_from", required = false) Date dateFrom,
			@RequestParam(name = "date_to", required = false) Date dateTo,
			@RequestParam(name = "offset", required = false) Integer offset,
			@RequestParam(name = "limit", required = false) Integer limit) {
		
		// Disregard parameters and return the one unconditionally
		List<Location> locations = List.of(this.locationService.getLocation());
		OcpiResponse<List<Location>> ocpiResponse = new OcpiResponse<List<Location>>(locations, 1000, "OK", new Date());
		
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
		response.setHeader("X-Total-Count", String.valueOf(locations.size()));
		response.setHeader("X-Limit", String.valueOf(locations.size()));
		response.setStatus(HttpStatus.OK.value());
		
		return ocpiResponse;
	}
}
