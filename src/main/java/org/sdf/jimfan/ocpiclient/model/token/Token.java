package org.sdf.jimfan.ocpiclient.model.token;

import org.sdf.jimfan.ocpiclient.model.JsonFormattable;
import org.sdf.jimfan.ocpiclient.model.datatype.CountryCode;
import org.sdf.jimfan.ocpiclient.model.datatype.EnergyContract;
import org.sdf.jimfan.ocpiclient.model.datatype.LanguageCode;
import org.sdf.jimfan.ocpiclient.model.datatype.ProfileType;
import org.sdf.jimfan.ocpiclient.model.datatype.TokenType;
import org.sdf.jimfan.ocpiclient.model.datatype.WhitelistType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

public class Token extends JsonFormattable {
	
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
	
	@JsonProperty("visual_number")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String visualNumber;
	
	@JsonProperty("issuer")
	private String issuer;
	
	@JsonProperty("group_id")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String groupId;
	
	@JsonProperty("valid")
	private Boolean valid;
	
	@JsonProperty("whitelist")
	private WhitelistType whitelist;
	
	@JsonProperty("language")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private LanguageCode language;
	
	@JsonProperty("default_profile_type")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private ProfileType defaultProfileType;
	
	@JsonProperty("energy_contract")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private EnergyContract energyContract;
	
	@JsonProperty("last_updated")
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
	private Date lastUpdated;

	public Token(CountryCode countryCode, String partyId, String uid, TokenType tokenType, String contractId,
			String issuer, Boolean valid, WhitelistType whitelist, Date lastUpdated) {
		this.countryCode = countryCode;
		this.partyId = partyId;
		this.uid = uid;
		this.tokenType = tokenType;
		this.contractId = contractId;
		this.issuer = issuer;
		this.valid = valid;
		this.whitelist = whitelist;
		this.lastUpdated = lastUpdated;
	}
	
	public CountryCode getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(CountryCode countryCode) {
		this.countryCode = countryCode;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public TokenType getTokenType() {
		return tokenType;
	}

	public void setTokenType(TokenType tokenType) {
		this.tokenType = tokenType;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getVisualNumber() {
		return visualNumber;
	}

	public void setVisualNumber(String visualNumber) {
		this.visualNumber = visualNumber;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public String getGroup_id() {
		return groupId;
	}

	public void setGroup_id(String group_id) {
		this.groupId = group_id;
	}

	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	public WhitelistType getWhitelist() {
		return whitelist;
	}

	public void setWhitelist(WhitelistType whitelist) {
		this.whitelist = whitelist;
	}

	public LanguageCode getLanguage() {
		return language;
	}

	public void setLanguage(LanguageCode language) {
		this.language = language;
	}

	public ProfileType getDefaultProfileType() {
		return defaultProfileType;
	}

	public void setDefaultProfileType(ProfileType defaultProfileType) {
		this.defaultProfileType = defaultProfileType;
	}

	public EnergyContract getEnergyContract() {
		return energyContract;
	}

	public void setEnergyContract(EnergyContract energyContract) {
		this.energyContract = energyContract;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
}
