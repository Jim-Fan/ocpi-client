package org.sdf.jimfan.ocpiclient.model.datatypes;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatusSchedule {

	@JsonProperty("period_begin")
	private final Date periodBegin;

	@JsonProperty("period_end")
	private final Date periodEnd;

	@JsonProperty("status")
	private final Status status;

	public StatusSchedule(Date periodBegin, Date periodEnd, Status status) {
		this.periodBegin = periodBegin;
		this.periodEnd = periodEnd;
		this.status = status;
	}

	public Date getPeriodBegin() {
		return periodBegin;
	}

	public Date getPeriodEnd() {
		return periodEnd;
	}

	public Status getStatus() {
		return status;
	}	
}
