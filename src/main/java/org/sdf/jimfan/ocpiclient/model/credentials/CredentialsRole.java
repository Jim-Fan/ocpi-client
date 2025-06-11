package org.sdf.jimfan.ocpiclient.model.credentials;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CredentialsRole {

	@JsonProperty("role")
	public Role role;
	
	@JsonProperty("business_details")
	public BusinessDetails businessDetails;
	
	@JsonProperty("party_id")
	public String partyId;
	
	@JsonProperty("country_code")
	public String countryCode;

	public CredentialsRole(Role role, BusinessDetails businessDetails, String partyId, String countryCode) {
		this.role = role;
		this.businessDetails = businessDetails;
		this.partyId = partyId;
		this.countryCode = countryCode;
	}
}
