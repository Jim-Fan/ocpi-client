package org.sdf.jimfan.ocpiclient.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.annotation.JsonProperty;

@RestController
@EnableAutoConfiguration
@ComponentScan
@Scope(value=WebApplicationContext.SCOPE_APPLICATION)
public class VersionDetailsController {

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
	
	static final String applicationDomain = System.getenv("AWS_APPRUNNER_DOMAIN");
	
	static final VersionDetails versionDetailsResult = new VersionDetails("2.2.1", new Endpoint[] {
			new Endpoint("credentials", "SENDER", "https://" + applicationDomain + "/ocpi/2.2.1/credentials"),
			new Endpoint("credentials", "RECEIVER", "https://" + applicationDomain + "/ocpi/2.2.1/credentials"),
			new Endpoint("tokens", "RECEIVER", "https://" + applicationDomain + "/ocpi/2.2.1/tokens")
		});
			
	
	/**
	 * Return version number with list of endpoint URLs e.g. /credentials, /tokens. For unknown reason
	 * the EMSP software I work uses trailing slash in the request. So both cases are accepted here.
	 */
	@GetMapping({"/ocpi/2.2.1", "/ocpi/2.2.1/"})
	public VersionDetails getVersionDetails() {
		return versionDetailsResult;
	}
}
