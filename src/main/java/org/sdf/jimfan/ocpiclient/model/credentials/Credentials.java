package org.sdf.jimfan.ocpiclient.model.credentials;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Credentials {

	@JsonProperty("token")
	public String token;
	
	@JsonProperty("url")
	public String url;
	
	@JsonProperty("roles")
	public CredentialsRole[] roles;

	public Credentials(String token, String url, CredentialsRole[] roles) {
		this.token = token;
		this.url = url;
		this.roles = roles;
	}
}
