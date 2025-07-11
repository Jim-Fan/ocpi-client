package org.sdf.jimfan.ocpiclient.model.datatype;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CdrDimension {

	@JsonProperty("type")
	private CdrDimensionType type;
	
	@JsonProperty("volume")
	private Float volume;

	public CdrDimension(CdrDimensionType type, Float volume) {
		super();
		this.type = type;
		this.volume = volume;
	}

	public CdrDimensionType getType() {
		return type;
	}

	public Float getVolume() {
		return volume;
	}
}
