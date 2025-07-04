package org.sdf.jimfan.ocpiclient.model.datatypes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class EnergyMix {

	@JsonProperty("is_green_energy")
	private Boolean isGreenEnergy;
	
	@JsonProperty("energy_source")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<EnergySource> energySources;
	
	@JsonProperty("environ_impact")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private EnvironmentalImpact environmentalImpact;
	
	@JsonProperty("supplier_name")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String supplierName;
	
	@JsonProperty("energy_product_name")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String energyProductName;

	public EnergyMix(Boolean isGreenEnergy, List<EnergySource> energySources, EnvironmentalImpact environmentalImpact,
			String supplierName, String energyProductName) {
		super();
		this.isGreenEnergy = isGreenEnergy;
		this.energySources = energySources;
		this.environmentalImpact = environmentalImpact;
		this.supplierName = supplierName;
		this.energyProductName = energyProductName;
	}
	
	public Boolean getIsGreenEnergy() {
		return isGreenEnergy;
	}

	public List<EnergySource> getEnergySources() {
		return this.energySources;
	}

	public EnvironmentalImpact getEnvironmentalImpact() {
		return environmentalImpact;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public String getEnergyProductName() {
		return energyProductName;
	}
}
