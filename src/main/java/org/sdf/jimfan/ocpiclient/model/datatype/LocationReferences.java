package org.sdf.jimfan.ocpiclient.model.datatype;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LocationReferences {

	@JsonProperty("location_id")
	private String locationId;

	@JsonProperty("evse_uids")
	private List<String> evseUids;
	
	public LocationReferences(String locationId, List<String> evseUids) {
		this.locationId = locationId;
		this.evseUids = evseUids;
	}
	
	public String getLocationId() {
		return locationId;
	}

	public List<String> getEvseUids() {
		return evseUids;
	}
}
