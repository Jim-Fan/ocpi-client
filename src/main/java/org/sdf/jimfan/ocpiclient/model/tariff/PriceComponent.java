package org.sdf.jimfan.ocpiclient.model.tariff;

import org.sdf.jimfan.ocpiclient.model.JsonFormattable;
import org.sdf.jimfan.ocpiclient.model.datatype.TariffDimensionType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PriceComponent extends JsonFormattable {

	@JsonProperty("type")
	private TariffDimensionType type;
	
	@JsonProperty("price")
	private TariffDimensionType price;
	
	@JsonProperty("vat")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Float vat;
	
	@JsonProperty("step_size")
	private Integer stepSize;
	
	public PriceComponent(TariffDimensionType type, TariffDimensionType price, Integer stepSize) {
		this.type = type;
		this.price = price;
		this.stepSize = stepSize;
	}

	public TariffDimensionType getType() {
		return type;
	}

	public void setType(TariffDimensionType type) {
		this.type = type;
	}

	public TariffDimensionType getPrice() {
		return price;
	}

	public void setPrice(TariffDimensionType price) {
		this.price = price;
	}

	public Float getVat() {
		return vat;
	}

	public void setVat(Float vat) {
		this.vat = vat;
	}

	public Integer getStepSize() {
		return stepSize;
	}

	public void setStepSize(Integer stepSize) {
		this.stepSize = stepSize;
	}
	
}
