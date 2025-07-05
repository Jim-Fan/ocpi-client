package org.sdf.jimfan.ocpiclient.controller;

import org.sdf.jimfan.ocpiclient.model.OcpiResponse;
import org.sdf.jimfan.ocpiclient.service.OcpiLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;

/**
 * OCPI 2.2.1 section 8.2
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
	public OcpiResponse getLocations(
			@RequestParam(name = "date_from", required = false) Date dateFrom,
			@RequestParam(name = "date_to", required = false) Date dateTo,
			@RequestParam(name = "offset", required = false) Integer offset,
			@RequestParam(name = "limit", required = false) Integer limit) {
	
		// Disregard parameters and return all locations unconditionally
		return new OcpiResponse(this.locationService.getLocations(), 1000, "OK", new Date());
	}
}
