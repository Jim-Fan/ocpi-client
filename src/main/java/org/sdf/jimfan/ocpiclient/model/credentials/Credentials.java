package org.sdf.jimfan.ocpiclient.model.credentials;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Credentials {

	@JsonProperty("token")
	private String token;
	
	@JsonProperty("url")
	private String url;
	
	@JsonProperty("roles")
	private CredentialsRole[] roles;

	public Credentials(String token, String url, CredentialsRole[] roles) {
		this.token = token;
		this.url = url;
		this.roles = roles;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public CredentialsRole[] getRoles() {
		return roles;
	}

	public void setRoles(CredentialsRole[] roles) {
		this.roles = roles;
	}
}
