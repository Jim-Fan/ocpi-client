package org.sdf.jimfan.ocpiclient.model.locations;

import org.sdf.jimfan.ocpiclient.model.datatypes.ConnectorFormat;
import org.sdf.jimfan.ocpiclient.model.datatypes.ConnectorType;
import org.sdf.jimfan.ocpiclient.model.datatypes.PowerType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URL;
import java.util.Date;
import java.util.List;


public class Connector {

	/**
	 * OCPI 2.2.1:
	 * 
	 * Identifier of the Connector within the EVSE. Two Connectors may have the same id as long
	 * as they do not belong to the same EVSE object.
	 */
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("standard")
	private ConnectorType standard;
	
	@JsonProperty("format")
	private ConnectorFormat format;
	
	@JsonProperty("power_type")
	private PowerType powerType;
	
	@JsonProperty("max_voltage")
	private Integer maxVoltage;
	
	@JsonProperty("max_amperage")
	private Integer maxAmperage;
	
	@JsonProperty("max_electric_power")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer maxElectricPower;

	@JsonProperty("tariff_ids")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<String> tariffIds;
	
	@JsonProperty("terms_and_conditions")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private URL url;
	
	@JsonProperty("last_updated")
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
	private Date lastUpdated;

	public Connector(String id, ConnectorType standard, ConnectorFormat format, PowerType powerType, Integer maxVoltage,
			Integer maxAmperage, Date lastUpdated) {
		this.id = id;
		this.standard = standard;
		this.format = format;
		this.powerType = powerType;
		this.maxVoltage = maxVoltage;
		this.maxAmperage = maxAmperage;
		this.lastUpdated = lastUpdated;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ConnectorType getStandard() {
		return standard;
	}

	public void setStandard(ConnectorType standard) {
		this.standard = standard;
	}

	public ConnectorFormat getFormat() {
		return format;
	}

	public void setFormat(ConnectorFormat format) {
		this.format = format;
	}

	public PowerType getPowerType() {
		return powerType;
	}

	public void setPowerType(PowerType powerType) {
		this.powerType = powerType;
	}

	public Integer getMaxVoltage() {
		return maxVoltage;
	}

	public void setMaxVoltage(Integer maxVoltage) {
		this.maxVoltage = maxVoltage;
	}

	public Integer getMaxAmperage() {
		return maxAmperage;
	}

	public void setMaxAmperage(Integer maxAmperage) {
		this.maxAmperage = maxAmperage;
	}

	public Integer getMaxElectricPower() {
		return maxElectricPower;
	}

	public void setMaxElectricPower(Integer maxElectricPower) {
		this.maxElectricPower = maxElectricPower;
	}

	public List<String> getTariffIds() {
		return tariffIds;
	}

	public void setTariffIds(List<String> tariffIds) {
		this.tariffIds = tariffIds;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
}
