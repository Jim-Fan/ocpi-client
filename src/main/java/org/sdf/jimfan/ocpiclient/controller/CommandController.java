package org.sdf.jimfan.ocpiclient.controller;

import java.util.Date;
import java.util.Map;

import org.sdf.jimfan.ocpiclient.exception.OcpiException;
import org.sdf.jimfan.ocpiclient.model.OcpiResponse;
import org.sdf.jimfan.ocpiclient.model.command.StartSessionCommand;
import org.sdf.jimfan.ocpiclient.service.OcpiSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@EnableAutoConfiguration
@ComponentScan
@Scope(value=WebApplicationContext.SCOPE_APPLICATION)
public class CommandController {

	private final Logger logger = LoggerFactory.getLogger(CommandController.class);
	
	@Autowired
	private OcpiSessionService sessionService;
	
	@PostMapping("/ocpi/2.2.1/commands/START_SESSION")
	public OcpiResponse<Map<String, String>> receiveToken(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestBody StartSessionCommand startSessionRequest) {
		
		logger.info("Start session request = {}", startSessionRequest);
		
		// TODO: Validate location, token ... etc
		
		OcpiResponse<Map<String, String>> ocpiResponse = new OcpiResponse<Map<String, String>>(
				Map.of("result", "FAILED"),
				3000,
				"This endpoint is under implementation",
				new Date());
		return ocpiResponse;
	}
}
