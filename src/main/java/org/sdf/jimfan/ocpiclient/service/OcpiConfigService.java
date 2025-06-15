package org.sdf.jimfan.ocpiclient.service;

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
	public void setMyApplicationProtocol(String myApplicationProtocol) {
		this.myApplicationProtocol = myApplicationProtocol;
	}
	public String getMyApplicationDomain() {
		return myApplicationDomain;
	}
	public void setMyApplicationDomain(String myApplicationDomain) {
		this.myApplicationDomain = myApplicationDomain;
	}
	public String getMyOcpiHandshakeToken() {
		return myOcpiHandshakeToken;
	}
	public void setMyOcpiHandshakeToken(String myOcpiHandshakeToken) {
		this.myOcpiHandshakeToken = myOcpiHandshakeToken;
	}
	public String getMyOcpiCredentialToken() {
		return myOcpiCredentialToken;
	}
	public void setMyOcpiCredentialToken(String myOcpiCredentialToken) {
		this.myOcpiCredentialToken = myOcpiCredentialToken;
	}
	public String getMyOcpiPartyId() {
		return myOcpiPartyId;
	}
	public void setMyOcpiPartyId(String myOcpiPartyId) {
		this.myOcpiPartyId = myOcpiPartyId;
	}
	public String getMyOcpiCountryCode() {
		return myOcpiCountryCode;
	}
	public void setMyOcpiCountryCode(String myOcpiCountryCode) {
		this.myOcpiCountryCode = myOcpiCountryCode;
	}
	public String getTheirOcpiCredentialsUrl() {
		return theirOcpiCredentialsUrl;
	}
	public void setTheirOcpiCredentialsUrl(String theirOcpiCredentialsUrl) {
		this.theirOcpiCredentialsUrl = theirOcpiCredentialsUrl;
	}
	public String getTheirOcpiCredentialToken() {
		return theirOcpiCredentialToken;
	}
	public void setTheirOcpiCredentialToken(String theirOcpiCredentialToken) {
		this.theirOcpiCredentialToken = theirOcpiCredentialToken;
	}
}
