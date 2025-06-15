package org.sdf.jimfan.ocpiclient.model.locations;

import org.sdf.jimfan.ocpiclient.model.credentials.BusinessDetails;
import org.sdf.jimfan.ocpiclient.model.datatypes.AdditionalGeoLocation;
import org.sdf.jimfan.ocpiclient.model.datatypes.CountryCode;
import org.sdf.jimfan.ocpiclient.model.datatypes.DisplayText;
import org.sdf.jimfan.ocpiclient.model.datatypes.EnergyMix;
import org.sdf.jimfan.ocpiclient.model.datatypes.Facility;
import org.sdf.jimfan.ocpiclient.model.datatypes.GeoLocation;
import org.sdf.jimfan.ocpiclient.model.datatypes.Hours;
import org.sdf.jimfan.ocpiclient.model.datatypes.Image;
import org.sdf.jimfan.ocpiclient.model.datatypes.ParkingType;
import org.sdf.jimfan.ocpiclient.model.datatypes.PublishTokenType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class Location {

	@JsonProperty("country_code")
	private String countryCode;
	
	@JsonProperty("party_id")
	private String partyId;
	
	@JsonProperty("id")
	private String locationId;
	
	@JsonProperty("publish")
	private boolean publish;
	
	@JsonProperty("publish_allowed_to")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<PublishTokenType> publishAllowedTo;
	
	@JsonProperty("name")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String name;
	
	@JsonProperty("address")
	private String address;
	
	@JsonProperty("city")
	private String city;
	
	@JsonProperty("postal_code")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String postalCode;
	
	@JsonProperty("state")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String state;
	
	@JsonProperty("country")
	private CountryCode country;
	
	@JsonProperty("coordinates")
	private GeoLocation coordinates;
	
	@JsonProperty("related_locations")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<AdditionalGeoLocation> relatedLocations;

	@JsonProperty("parking_type")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private ParkingType parkingType;

	@JsonProperty("evses")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<EVSE> evses;
	
	@JsonProperty("directions")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<DisplayText> directions;

	@JsonProperty("operator")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private BusinessDetails operator;
	
	@JsonProperty("sub_operator")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private BusinessDetails subOperator;

	@JsonProperty("owner")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private BusinessDetails owner;
	
	@JsonProperty("facilities")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<Facility> facilities;
	
	@JsonProperty("time_zone")
	@JsonFormat(shape=JsonFormat.Shape.STRING)
	private TimeZone timezone;
	
	@JsonProperty("opening_times")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Hours openingTimes;

	@JsonProperty("charging_when_closed")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Boolean chargingWhenClosed;
	
	@JsonProperty("images")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<Image> images;

	@JsonProperty("energy_mix")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private EnergyMix energyMix;
	
	@JsonProperty("last_updated")
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
	private Date lastUpdated;
	
	
	/**
	 * Constructor. Parameters are mandatory fields according to OCPI 2.2.1
	 */
	public Location(String countryCode, String partyId, String locationId, Boolean publish, String address, String city,
			CountryCode country, GeoLocation coordinates, TimeZone timeZone, Date lastUpdated) {
		this.countryCode = countryCode;
		this.partyId = partyId;
		this.locationId = locationId;
		this.publish = publish;
		this.address = address;
		this.city = city;
		this.country = country;
		this.coordinates = coordinates;
		this.timezone = timeZone;
		this.lastUpdated = lastUpdated;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public String getLocationId() {
		return locationId;
	}


	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public boolean isPublish() {
		return publish;
	}

	public void setPublish(boolean publish) {
		this.publish = publish;
	}

	public List<PublishTokenType> getPublishAllowedTo() {
		return publishAllowedTo;
	}

	public void setPublishAllowedTo(List<PublishTokenType> publishAllowedTo) {
		this.publishAllowedTo = publishAllowedTo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public CountryCode getCountry() {
		return country;
	}

	public void setCountry(CountryCode country) {
		this.country = country;
	}

	public GeoLocation getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(GeoLocation coordinates) {
		this.coordinates = coordinates;
	}

	public List<AdditionalGeoLocation> getRelatedLocations() {
		return relatedLocations;
	}

	public void setRelatedLocations(List<AdditionalGeoLocation> relatedLocations) {
		this.relatedLocations = relatedLocations;
	}

	public ParkingType getParkingType() {
		return parkingType;
	}

	public void setParkingType(ParkingType parkingType) {
		this.parkingType = parkingType;
	}

	public List<EVSE> getEvses() {
		return evses;
	}

	public void setEvses(List<EVSE> evses) {
		this.evses = evses;
	}

	public List<DisplayText> getDirections() {
		return directions;
	}

	public void setDirections(List<DisplayText> directions) {
		this.directions = directions;
	}

	public BusinessDetails getOperator() {
		return operator;
	}

	public void setOperator(BusinessDetails operator) {
		this.operator = operator;
	}

	public BusinessDetails getSubOperator() {
		return subOperator;
	}

	public void setSubOperator(BusinessDetails subOperator) {
		this.subOperator = subOperator;
	}

	public BusinessDetails getOwner() {
		return owner;
	}

	public void setOwner(BusinessDetails owner) {
		this.owner = owner;
	}

	public List<Facility> getFacilities() {
		return facilities;
	}

	public void setFacilities(List<Facility> facilities) {
		this.facilities = facilities;
	}

	public TimeZone getTimezone() {
		return timezone;
	}

	public void setTimezone(TimeZone timezone) {
		this.timezone = timezone;
	}

	public Hours getOpeningTimes() {
		return openingTimes;
	}

	public void setOpeningTimes(Hours openingTimes) {
		this.openingTimes = openingTimes;
	}

	public Boolean getChargingWhenClosed() {
		return chargingWhenClosed;
	}

	public void setChargingWhenClosed(Boolean chargingWhenClosed) {
		this.chargingWhenClosed = chargingWhenClosed;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public EnergyMix getEnergyMix() {
		return energyMix;
	}

	public void setEnergyMix(EnergyMix energyMix) {
		this.energyMix = energyMix;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
}
