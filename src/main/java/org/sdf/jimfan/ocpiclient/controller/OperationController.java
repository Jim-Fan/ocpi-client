package org.sdf.jimfan.ocpiclient.controller;

import org.sdf.jimfan.ocpiclient.service.OcpiCredentialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;


/**
 * Interface for triggering operations by the OCPI client e.g. perform handshake, update config.
 */
@RestController
@EnableAutoConfiguration
@ComponentScan
@Scope(value=WebApplicationContext.SCOPE_APPLICATION)
public class OperationController {

	static final Logger logger = LoggerFactory.getLogger(OperationController.class);
	
	@Autowired
	private OcpiCredentialService credentialService;
	
	@GetMapping("/op/handshake")
	public String performHandshake() {
		
		try {
			String response = this.credentialService.performHandshake();
			return response;
		}
		catch (Exception ex) {
			return ex.getMessage();
		}
	}
}
