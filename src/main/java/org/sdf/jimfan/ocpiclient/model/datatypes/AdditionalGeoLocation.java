package org.sdf.jimfan.ocpiclient.model.datatypes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AdditionalGeoLocation extends GeoLocation {

	@JsonProperty("name")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private DisplayText name;
	
	public AdditionalGeoLocation(DisplayText name, String latitude, String longitude) {
		super(latitude, longitude);
		this.name = name;
	}

	public DisplayText getName() {
		return name;
	}
}
