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
public class VersionController {
	
	static class OcpiVersion {
		
		@JsonProperty("version")
		public String version;
		
		@JsonProperty("url")
		public String versionDetailsUrl;
		
		public OcpiVersion(String version, String versionDetailsUrl) {
			this.version = version;
			this.versionDetailsUrl = versionDetailsUrl;
		}
	}
	
	static class VersionResult {
		
		@JsonProperty("data")
		public OcpiVersion[] data;
		
		@JsonProperty("status_code")
		public int statusCode;
		
		@JsonProperty("status_message")
		public String statusMessage;
		
		@JsonProperty("timestamp")
		@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssZ")
		public Date timestamp;
		
		public VersionResult(OcpiVersion[] data, int statusCode, String statusMessage, Date timestamp) {
			this.data = data;
			this.statusCode = statusCode;
			this.statusMessage = statusMessage;
			this.timestamp = timestamp;
		}
	}
	
	/**
	 * Version URL domain is obtained from environment.
	 */
	static final String applicationDomain = System.getenv("AWS_APPRUNNER_DOMAIN");
	
	static final OcpiVersion[] supportedVersions = new OcpiVersion[] {
			new OcpiVersion("2.2.1", "https://" + applicationDomain + "/ocpi/2.2.1")
		};
	
	/**
	 * Return supported version. It is intended only 2.2.1 is supported.
	 */
	@GetMapping("/ocpi/versions")
	public VersionResult getVersion() {
		return new VersionResult(
			supportedVersions,
			1000,
			"OK",
			new Date()
		);
	}
}
