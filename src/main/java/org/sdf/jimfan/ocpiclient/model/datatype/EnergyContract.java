package org.sdf.jimfan.ocpiclient.model.datatype;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class EnergyContract {

	@JsonProperty("supplier_name")
	private String supplierName;
	
	@JsonProperty("contract_id")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String contractId;
	
	@JsonProperty("location_id")
	private String locationId;
	
	@JsonProperty("evse_ids")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<String> evseIds;

	public EnergyContract(String supplierName, String contractId, String locationId, List<String> evseIds) {
		super();
		this.supplierName = supplierName;
		this.contractId = contractId;
		this.locationId = locationId;
		this.evseIds = evseIds;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public String getContractId() {
		return contractId;
	}

	public String getLocationId() {
		return locationId;
	}

	public List<String> getEvseIds() {
		return evseIds;
	}
}
