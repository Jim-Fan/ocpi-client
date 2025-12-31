package org.sdf.jimfan.ocpiclient.model.datatype;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;

public class StatusSchedule {

	@JsonProperty("period_begin")
	private final ZonedDateTime periodBegin;

	@JsonProperty("period_end")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private final ZonedDateTime periodEnd;

	@JsonProperty("status")
	private final Status status;

	public StatusSchedule(ZonedDateTime periodBegin, ZonedDateTime periodEnd, Status status) {
		this.periodBegin = periodBegin;
		this.periodEnd = periodEnd;
		this.status = status;
	}

	public ZonedDateTime getPeriodBegin() {
		return periodBegin;
	}

	public ZonedDateTime getPeriodEnd() {
		return periodEnd;
	}

	public Status getStatus() {
		return status;
	}	
}
