package org.sdf.jimfan.ocpiclient.model.datatype;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChargingPeriod {

	@JsonProperty("start_date_time")
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
	private Date startDateTime;
	
	@JsonProperty("dimensions")
	private List<CdrDimension> dimensions;
	
	@JsonProperty("tariff_id")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String tariffId;

	public ChargingPeriod(Date startDateTime, List<CdrDimension> dimensions, String tariffId) {
		super();
		this.startDateTime = startDateTime;
		this.dimensions = dimensions;
		this.tariffId = tariffId;
	}

	public Date getStartDateTime() {
		return startDateTime;
	}

	public List<CdrDimension> getDimensions() {
		return dimensions;
	}

	public String getTariffId() {
		return tariffId;
	}
}
