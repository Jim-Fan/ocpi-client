package org.sdf.jimfan.ocpiclient.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

@RestController
@EnableAutoConfiguration
@ComponentScan
@Scope(value=WebApplicationContext.SCOPE_APPLICATION)
public class VersionDetailController {

	static class Endpoint {

		@JsonProperty("identifier")
		public String identifier;

		@JsonProperty("role")
		public String role;
		
		@JsonProperty("url")
		public String url;
		
		public Endpoint(String identifier, String role, String url) {
			this.identifier = identifier;
			this.role = role;
			this.url = url;
		}
	}
	
	static class VersionDetails {
		
		@JsonProperty("version")
		public String version;

		@JsonProperty("endpoints")
		public Endpoint[] endpoints;
		
		public VersionDetails(String version, Endpoint[] endpoints) {
			this.version = version;
			this.endpoints = endpoints;
		}
	}
	
	static class VersionDetailsResult {
		
		@JsonProperty("data")
		public VersionDetails data;
		
		@JsonProperty("status_code")
		public int statusCode;
		
		@JsonProperty("status_message")
		public String statusMessage;
		
		@JsonProperty("timestamp")
		@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssZ")
		public Date timestamp;
		
		public VersionDetailsResult(VersionDetails data, int statusCode, String statusMessage, Date timestamp) {
			this.data = data;
			this.statusCode = statusCode;
			this.statusMessage = statusMessage;
			this.timestamp = timestamp;
		}
	}
	
	static final String applicationProtocol = System.getenv("APP_HTTP_PROTOCOL");
	static final String applicationDomain = System.getenv("APP_DOMAIN_NAME");
	
	static final VersionDetails versionDetailsResult = new VersionDetails("2.2.1", new Endpoint[] {
			new Endpoint("credentials", "SENDER", applicationProtocol + "://" + applicationDomain + "/ocpi/2.2.1/credentials"),
			new Endpoint("credentials", "RECEIVER", applicationProtocol + "://" + applicationDomain + "/ocpi/2.2.1/credentials"),
			new Endpoint("tokens", "RECEIVER", applicationProtocol + "://" + applicationDomain + "/ocpi/2.2.1/tokens")
		});
	
	/**
	 * Return version number with list of endpoint URLs e.g. /credentials, /tokens. For unknown reason
	 * the EMSP software I work uses trailing slash in the request. So both cases are accepted here.
	 */
	@GetMapping({"/ocpi/2.2.1", "/ocpi/2.2.1/"})
	public VersionDetailsResult getVersionDetails() {
		return new VersionDetailsResult(
			versionDetailsResult,
			1000,
			"OK", 
			new Date());
	}
}
