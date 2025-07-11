package org.sdf.jimfan.ocpiclient.model.token;

import org.sdf.jimfan.ocpiclient.model.JsonFormattable;
import org.sdf.jimfan.ocpiclient.model.datatype.CountryCode;
import org.sdf.jimfan.ocpiclient.model.datatype.TokenType;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CdrToken extends JsonFormattable {
	
	@JsonProperty("country_code")
	private CountryCode countryCode;
	
	@JsonProperty("party_id")
	private String partyId;
	
	@JsonProperty("uid")
	private String uid;
	
	@JsonProperty("type")
	private TokenType tokenType;
	
	@JsonProperty("contract_id")
	private String contractId;
	
	public CdrToken(CountryCode countryCode, String partyId, String uid, TokenType tokenType, String contractId) {
		this.countryCode = countryCode;
		this.partyId = partyId;
		this.uid = uid;
		this.tokenType = tokenType;
		this.contractId = contractId;
	}

	public CountryCode getCountryCode() {
		return countryCode;
	}

	public String getPartyId() {
		return partyId;
	}

	public String getUid() {
		return uid;
	}

	public TokenType getTokenType() {
		return tokenType;
	}

	public String getContractId() {
		return contractId;
	}
}
