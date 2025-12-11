package org.sdf.jimfan.ocpiclient.model.command;

import org.sdf.jimfan.ocpiclient.model.token.Token;
import org.sdf.jimfan.ocpiclient.model.JsonFormattable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URL;

public class StartSessionCommand extends JsonFormattable {

	@JsonProperty("response_url")
	private URL responseUrl;
	
	@JsonProperty("token")
	private Token token;
	
	@JsonProperty("location_id")
	private String locationId;
		
	@JsonProperty("evse_uid")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String evseUid;
	
	@JsonProperty("connector_id")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String connectorId;
		
	@JsonProperty("authorization_reference")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String authorizationReference;

	public StartSessionCommand(URL responseUrl, Token token, String locationId, String evseUid, String connectorId, String authorizationReference) {
		this.responseUrl = responseUrl;
		this.token = token;
		this.locationId = locationId;
		this.evseUid = evseUid;
		this.connectorId = connectorId;
		this.authorizationReference = authorizationReference;
	}
	
	public URL getResponseUrl() {
		return responseUrl;
	}

	public void setResponseUrl(URL responseUrl) {
		this.responseUrl = responseUrl;
	}

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String getEvseUid() {
		return evseUid;
	}

	public void setEvseUid(String evseUid) {
		this.evseUid = evseUid;
	}

	public String getConnectorId() {
		return connectorId;
	}

	public void setConnectorId(String connectorId) {
		this.connectorId = connectorId;
	}

	public String getAuthorizationReference() {
		return authorizationReference;
	}

	public void setAuthorizationReference(String authorizationReference) {
		this.authorizationReference = authorizationReference;
	}
	
	
}
