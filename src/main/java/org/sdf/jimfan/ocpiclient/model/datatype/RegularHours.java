package org.sdf.jimfan.ocpiclient.model.datatype;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegularHours {

	@JsonProperty("weekday")
	private Integer weekday;
	
	@JsonProperty("period_begin")
	private String periodBegin;

	@JsonProperty("period_end")
	private String periodEnd;
	
	public RegularHours(Integer weekday, String periodBegin, String periodEnd) {
		this.weekday = weekday;
		this.periodBegin = periodBegin;
		this.periodEnd = periodEnd;
	}

	public Integer getWeekday() {
		return weekday;
	}
	
	public String getPeriodBegin() {
		return periodBegin;
	}

	public String getPeriodEnd() {
		return periodEnd;
	}
}
