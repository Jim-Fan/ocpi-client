package org.sdf.jimfan.ocpiclient.model.datatype;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EnvironmentalImpact {

	@JsonProperty("category")
	private EnvironmentalImpactCategory category;
	
	@JsonProperty("amount")
	private Float amount;

	public EnvironmentalImpact(EnvironmentalImpactCategory category, Float amount) {
		this.category = category;
		this.amount = amount;
	}

	public EnvironmentalImpactCategory getCategory() {
		return category;
	}

	public Float getAmount() {
		return amount;
	}
}
