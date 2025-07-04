package org.sdf.jimfan.ocpiclient.model.datatypes;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GeoLocation {

	@JsonProperty("latitude")
	private String latitude;
	
	@JsonProperty("longitude")
	private String longitude;

	public GeoLocation(String latitude, String longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public String getLatitude() {
		return latitude;
	}

	public String getLongitude() {
		return longitude;
	}
}
