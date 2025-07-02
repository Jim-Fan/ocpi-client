package org.sdf.jimfan.ocpiclient.service;

import java.util.Base64;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

@Service
@Scope(value=WebApplicationContext.SCOPE_APPLICATION)
public class OcpiConfigService {

	// Init with environment, but updatable as application runs
	private String myApplicationProtocol = System.getenv("APP_HTTP_PROTOCOL");
	private String myApplicationDomain = System.getenv("APP_DOMAIN_NAME");
	private String myOcpiHandshakeToken = System.getenv("OCPI_TOKEN_A");
	private String myOcpiCredentialToken = System.getenv("OCPI_TOKEN_C");
	private String myOcpiPartyId = System.getenv("OCPI_PARTY_ID");
	private String myOcpiCountryCode = System.getenv("OCPI_COUNTRY_CODE");
	private String theirOcpiCredentialsUrl = System.getenv("OCPI_SERVER_CREDENTIALS_URL");
	private String theirOcpiCredentialToken = System.getenv("OCPI_TOKEN_B");
	
	public String getMyApplicationProtocol() {
		return myApplicationProtocol;
	}
	
	public String getMyApplicationDomain() {
		return myApplicationDomain;
	}
	
	public String getMyOcpiHandshakeToken() {
		return myOcpiHandshakeToken;
	}
	
	public String getMyOcpiCredentialToken() {
		return myOcpiCredentialToken;
	}
	
	public String getMyOcpiCredentialTokenEncoded() {
		if (myOcpiCredentialToken == null || myOcpiCredentialToken.length() <= 0) {
			return "";
		}
		return new String(Base64.getEncoder().encode(myOcpiCredentialToken.getBytes()));
	}
	
	public void setMyOcpiCredentialToken(String newTokenC) {
		this.myOcpiCredentialToken = newTokenC;
	}
	
	public String getMyOcpiPartyId() {
		return myOcpiPartyId;
	}
	
	public String getMyOcpiCountryCode() {
		return myOcpiCountryCode;
	}
	
	public String getTheirOcpiCredentialsUrl() {
		return theirOcpiCredentialsUrl;
	}
	
	public String getTheirOcpiCredentialToken() {
		return theirOcpiCredentialToken;
	}
}
