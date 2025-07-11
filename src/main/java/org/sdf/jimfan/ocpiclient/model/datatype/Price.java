package org.sdf.jimfan.ocpiclient.model.datatype;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Price {

	@JsonProperty("excl_vat")
	private Float excludeVat;
	
	@JsonProperty("incl_vat")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Float includeVat;

	public Price(Float excludeVat, Float includeVat) {
		super();
		this.excludeVat = excludeVat;
		this.includeVat = includeVat;
	}

	public Float getExcludeVat() {
		return excludeVat;
	}

	public Float getIncludeVat() {
		return includeVat;
	}
}
