package org.sdf.jimfan.ocpiclient.model.datatype;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExceptionPeriod {

	@JsonProperty("period_begin")
	private ZonedDateTime periodBegin;
	
	@JsonProperty("period_end")
	private ZonedDateTime periodEnd;

	public ExceptionPeriod(ZonedDateTime periodBegin, ZonedDateTime periodEnd) {
		this.periodBegin = periodBegin;
		this.periodEnd = periodEnd;
	}

	public ZonedDateTime getPeriodBegin() {
		return periodBegin;
	}
	
	public ZonedDateTime getPeriodEnd() {
		return periodEnd;
	}
}
