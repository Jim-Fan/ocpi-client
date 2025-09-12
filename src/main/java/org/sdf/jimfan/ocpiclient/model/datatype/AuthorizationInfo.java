package org.sdf.jimfan.ocpiclient.model.datatype;

import org.sdf.jimfan.ocpiclient.model.token.Token;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthorizationInfo {

	@JsonProperty("allowed")
	private AllowedType allowed;
	
	@JsonProperty("token")
	private Token token;
	
	@JsonProperty("location")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private LocationReferences location;
	
	@JsonProperty("authorization_reference")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String authorizationReference;
	
	@JsonProperty("info")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private DisplayText info;
	
	public AuthorizationInfo(AllowedType allowed, Token token) {
		this.allowed = allowed;
		this.token = token;
	}

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public LocationReferences getLocation() {
		return location;
	}

	public void setLocation(LocationReferences location) {
		this.location = location;
	}

	public DisplayText getInfo() {
		return info;
	}

	public void setInfo(DisplayText info) {
		this.info = info;
	}

	public AllowedType getAllowed() {
		return allowed;
	}

	public String getAuthorizationReference() {
		return authorizationReference;
	}
}
