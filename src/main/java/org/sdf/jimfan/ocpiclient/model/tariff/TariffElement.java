package org.sdf.jimfan.ocpiclient.model.tariff;

import org.sdf.jimfan.ocpiclient.model.JsonFormattable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TariffElement extends JsonFormattable {

	@JsonProperty("price_components")
	private List<PriceComponent> priceComponents;
	
	@JsonProperty("restrictions")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private TariffRestrictions restrictions;

	public TariffElement(List<PriceComponent> priceComponents) {
		this.priceComponents = priceComponents;
	}
	
	public List<PriceComponent> getPriceComponents() {
		return priceComponents;
	}

	public void setPriceComponents(List<PriceComponent> priceComponents) {
		this.priceComponents = priceComponents;
	}

	public TariffRestrictions getRestrictions() {
		return restrictions;
	}

	public void setRestrictions(TariffRestrictions restrictions) {
		this.restrictions = restrictions;
	}
}
