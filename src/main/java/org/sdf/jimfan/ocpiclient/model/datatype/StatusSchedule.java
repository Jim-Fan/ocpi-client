package org.sdf.jimfan.ocpiclient.model.datatype;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StatusSchedule {

	@JsonProperty("period_begin")
	private final Date periodBegin;

	@JsonProperty("period_end")
	@JsonInclude(JsonInclude.Include.NON_NULL)
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
