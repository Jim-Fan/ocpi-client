package org.sdf.jimfan.ocpiclient.model.tariff;

import org.sdf.jimfan.ocpiclient.model.JsonFormattable;
import org.sdf.jimfan.ocpiclient.model.datatype.CountryCode;
import org.sdf.jimfan.ocpiclient.model.datatype.DisplayText;
import org.sdf.jimfan.ocpiclient.model.datatype.EnergyMix;
import org.sdf.jimfan.ocpiclient.model.datatype.Price;
import org.sdf.jimfan.ocpiclient.model.datatype.TariffType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.Currency;
import java.util.List;

public class Tariff extends JsonFormattable {

	@JsonProperty("country_code")
	private CountryCode countryCode;
	
	@JsonProperty("party_id")
	private String partyId;
	
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("currency")
	private Currency currency;
	
	@JsonProperty("type")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private TariffType type;
	
	@JsonProperty("tariff_alt_text")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private DisplayText tariffAltText;
	
	@JsonProperty("tariff_alt_url")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private URL tariffAltUrl;
	
	@JsonProperty("min_price")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Price minPrice;
	
	@JsonProperty("max_price")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Price maxPrice;
	
	@JsonProperty("elements")
	private List<TariffElement> elements;
	
	@JsonProperty("start_date_time")
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private ZonedDateTime startDateTime;
	
	@JsonProperty("end_date_time")
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private ZonedDateTime endDateTime;
	
	@JsonProperty("energy_mix")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private EnergyMix energyMix;
	
	@JsonProperty("last_updated")
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
	private ZonedDateTime lastUpdated;
	
	public Tariff(CountryCode countryCode, String partyId, String id, Currency currency, List<TariffElement> elements, ZonedDateTime lastUpdated) {
		this.countryCode = countryCode;
		this.partyId = partyId;
		this.id = id;
		this.currency = currency;
		this.elements = elements;
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

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public TariffType getType() {
		return type;
	}

	public void setType(TariffType type) {
		this.type = type;
	}

	public DisplayText getTariffAltText() {
		return tariffAltText;
	}

	public void setTariffAltText(DisplayText tariffAltText) {
		this.tariffAltText = tariffAltText;
	}

	public URL getTariffAltUrl() {
		return tariffAltUrl;
	}

	public void setTariffAltUrl(URL tariffAltUrl) {
		this.tariffAltUrl = tariffAltUrl;
	}

	public Price getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(Price minPrice) {
		this.minPrice = minPrice;
	}

	public Price getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(Price maxPrice) {
		this.maxPrice = maxPrice;
	}

	public List<TariffElement> getElements() {
		return elements;
	}

	public void setElements(List<TariffElement> elements) {
		this.elements = elements;
	}

	public ZonedDateTime getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(ZonedDateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	public ZonedDateTime getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(ZonedDateTime endDateTime) {
		this.endDateTime = endDateTime;
	}

	public EnergyMix getEnergyMix() {
		return energyMix;
	}

	public void setEnergyMix(EnergyMix energyMix) {
		this.energyMix = energyMix;
	}

	public ZonedDateTime getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(ZonedDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
}
