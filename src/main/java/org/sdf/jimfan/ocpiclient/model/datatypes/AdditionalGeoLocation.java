package org.sdf.jimfan.ocpiclient.model.datatypes;

public class AdditionalGeoLocation extends GeoLocation {

	// TODO: Implement this
	
	private DisplayText name;
	
	public AdditionalGeoLocation(DisplayText name, String latitude, String longitude) {
		super(latitude, longitude);
		this.name = name;
	}
}
