package org.sdf.jimfan.ocpiclient.controller;

import org.sdf.jimfan.ocpiclient.exception.OcpiException;
import org.sdf.jimfan.ocpiclient.model.OcpiResponse;
import org.sdf.jimfan.ocpiclient.model.command.CommandResponse;
import org.sdf.jimfan.ocpiclient.model.command.CommandResult;
import org.sdf.jimfan.ocpiclient.model.command.StartSessionCommand;
import org.sdf.jimfan.ocpiclient.model.datatype.CommandResponseType;
import org.sdf.jimfan.ocpiclient.model.datatype.CommandResultType;
import org.sdf.jimfan.ocpiclient.model.datatype.DisplayText;
import org.sdf.jimfan.ocpiclient.model.datatype.LanguageCode;
import org.sdf.jimfan.ocpiclient.service.OcpiConfigService;
import org.sdf.jimfan.ocpiclient.service.OcpiSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.ResponseSpec;
import org.springframework.web.context.WebApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;

@RestController
@EnableAutoConfiguration
@ComponentScan
@Scope(value=WebApplicationContext.SCOPE_APPLICATION)
public class CommandController {

	private final Logger logger = LoggerFactory.getLogger(CommandController.class);
	
	@Autowired
	private OcpiSessionService sessionService;
	
	@Autowired
	private TaskExecutor taskExecutor;
	
	@Autowired
	private OcpiConfigService configService;
	
	/**
	 * Command endpoint for starting charge session, usually invoked by MSP when, for
	 * example, itself or drivers are initiating charging. This function should:
	 * 
	 * 1 ) Return command response to initial inbound HTTP/POST
	 * 2 ) Directly or indirectly, initiate another HTTP/POST to MSP base on charger's outcome
	 * 
	 * @param request
	 * @param response
	 * @param startSessionRequest
	 * @return
	 */
	@PostMapping("/ocpi/2.2.1/commands/START_SESSION")
	public CommandResponse handleStartSessionRequest(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestBody StartSessionCommand startSessionRequest) {
		
		logger.info("Start session request = {}", startSessionRequest);
		
		// In real world CPO implementation, CPO would initiate RemoteStartTransaction to charger and post
		// charger's outcome to MSP through response URL
		final URL responseUrl = startSessionRequest.getResponseUrl();
		final String encodedToken = this.configService.getMyOcpiCredentialTokenEncoded();
		final String requestId = request.getHeader("X-Request-ID");
		final String correlationId = request.getHeader("X-Correlation-ID");
		
		boolean validationFailed = false;
		try {
			this.sessionService.validateStartSessionRequest(
					startSessionRequest.getToken(),
					startSessionRequest.getLocationId(),
					startSessionRequest.getEvseUid(),
					startSessionRequest.getConnectorId(),
					startSessionRequest.getResponseUrl().toString(),
					startSessionRequest.getAuthorizationReference());
		}
		catch (OcpiException ex) {
			logger.error("Start charge request validation failed: {}", ex.getMessage());
			validationFailed = true;
		}
		
		if (validationFailed) {
			return new CommandResponse(CommandResponseType.REJECTED, 15);
		}
		
		// Initiate charging in separate thread and send charger response in separate HTTP/POST
		this.taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				CommandResult commandResult = new CommandResult(CommandResultType.EVSE_INOPERATIVE, List.of(new DisplayText(LanguageCode.en, "Charge station is not yet usable")));
				RestClient restClient = RestClient.create();
				
				URI uri = null;
				try {
					uri = responseUrl.toURI();
				} catch (URISyntaxException e) {
					logger.error("Failed to convert URL {} to URI: ", responseUrl, e.getMessage());
					throw new RuntimeException(e);
				}
				
				logger.info("Posting to response URL {}", responseUrl);
				ResponseSpec spec = restClient
						.post()
						.uri(uri)
						.body(commandResult)  // type = CommandResult
						.contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", "Token " + encodedToken)    // required by OCPI 2.2.1
						.header("X-Request-ID", requestId)
						.header("X-Correlation-ID", correlationId)
						.retrieve();
				
				ResponseEntity<OcpiResponse<Map<String, String>>> ocpiResponse = spec.toEntity(new ParameterizedTypeReference<>() {});
				logger.info("HTTP status = {}", ocpiResponse.getStatusCode().toString());
				logger.info("HTTP headers = {}", ocpiResponse.getHeaders());
				logger.info("HTTP body = {}", ocpiResponse.getBody());
				
				OcpiResponse<Map<String, String>> body = ocpiResponse.getBody();
				Map<String, String> data = body.getData();
				logger.info("OCPI status = {}", body.getStatusCode());
				logger.info("OCPI message = {}", body.getStatusMessage());
				logger.info("OCPI data field = {}", data);
				logger.info("OCPI request accepted = {}", data.containsKey("result") && "ACCEPTED".equals(data.get("result")));
				
				// TODO: Initiate charging
				return;
			}
		});
		
		return new CommandResponse(CommandResponseType.ACCEPTED, 15);
	}
}
