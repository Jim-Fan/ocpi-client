package org.sdf.jimfan.ocpiclient.model.session;

import org.sdf.jimfan.ocpiclient.model.JsonFormattable;
import org.sdf.jimfan.ocpiclient.model.datatype.AuthMethod;
import org.sdf.jimfan.ocpiclient.model.datatype.ChargingPeriod;
import org.sdf.jimfan.ocpiclient.model.datatype.CountryCode;
import org.sdf.jimfan.ocpiclient.model.datatype.EnergyContract;
import org.sdf.jimfan.ocpiclient.model.datatype.LanguageCode;
import org.sdf.jimfan.ocpiclient.model.datatype.Price;
import org.sdf.jimfan.ocpiclient.model.datatype.ProfileType;
import org.sdf.jimfan.ocpiclient.model.datatype.SessionStatus;
import org.sdf.jimfan.ocpiclient.model.datatype.TokenType;
import org.sdf.jimfan.ocpiclient.model.datatype.WhitelistType;
import org.sdf.jimfan.ocpiclient.model.token.CdrToken;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Currency;
import java.util.Date;
import java.util.List;

public class Session extends JsonFormattable {

	@JsonProperty("country_code")
	private CountryCode countryCode;
	
	@JsonProperty("party_id")
	private String partyId;
	
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("start_date_time")
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
	private Date startDateTime;
	
	@JsonProperty("end_date_time")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
	private Date endDateTime;
	
	@JsonProperty("kwh")
	private Float kwh;
	
	@JsonProperty("cdr_token")
	private CdrToken cdrToken;
	
	@JsonProperty("auth_method")
	private AuthMethod authMethod;
	
	@JsonProperty("authorization_reference")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String authorizationReference;
	
	@JsonProperty("location_id")
	private String locationId;
	
	@JsonProperty("evse_uid")
	private String evseUid;
	
	@JsonProperty("connector_id")
	private String connectorId;
	
	@JsonProperty("meter_id")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String meterId;
	
	@JsonProperty("currency")
	private Currency currency;

	@JsonProperty("charging_periods")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<ChargingPeriod> chargePeriods;
	
	@JsonProperty("total_cost")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Price totalCost;
	
	@JsonProperty("status")
	private SessionStatus sessionStatus;
	
	@JsonProperty("last_updated")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
	private Date lastUpdated;

	
	public Session(CountryCode countryCode, String partyId, String id, Date startDateTime, Float kwh,
			CdrToken cdrToken, AuthMethod authMethod, String locationId, String evseUid,
			String connectorId, Currency currency,
			SessionStatus sessionStatus, Date lastUpdated) {
		this.countryCode = countryCode;
		this.partyId = partyId;
		this.id = id;
		this.startDateTime = startDateTime;
		this.kwh = kwh;
		this.cdrToken = cdrToken;
		this.authMethod = authMethod;
		this.locationId = locationId;
		this.evseUid = evseUid;
		this.connectorId = connectorId;
		this.currency = currency;
		this.sessionStatus = sessionStatus;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Date getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
	}

	public Float getKwh() {
		return kwh;
	}

	public void setKwh(Float kwh) {
		this.kwh = kwh;
	}

	public CdrToken getCdrToken() {
		return cdrToken;
	}

	public void setCdrToken(CdrToken cdrToken) {
		this.cdrToken = cdrToken;
	}

	public AuthMethod getAuthMethod() {
		return authMethod;
	}

	public void setAuthMethod(AuthMethod authMethod) {
		this.authMethod = authMethod;
	}

	public String getAuthorizationReference() {
		return authorizationReference;
	}

	public void setAuthorizationReference(String authorizationReference) {
		this.authorizationReference = authorizationReference;
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

	public String getMeterId() {
		return meterId;
	}

	public void setMeterId(String meterId) {
		this.meterId = meterId;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public List<ChargingPeriod> getChargePeriods() {
		return chargePeriods;
	}

	public void setChargePeriods(List<ChargingPeriod> chargePeriods) {
		this.chargePeriods = chargePeriods;
	}

	public Price getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(Price totalCost) {
		this.totalCost = totalCost;
	}

	public SessionStatus getSessionStatus() {
		return sessionStatus;
	}

	public void setSessionStatus(SessionStatus sessionStatus) {
		this.sessionStatus = sessionStatus;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
}
