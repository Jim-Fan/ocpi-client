package org.sdf.jimfan.ocpiclient.model.datatypes;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

public class ExceptionPeriod {

	@JsonProperty("period_begin")
	private Date periodBegin;
	
	@JsonProperty("period_end")
	private Date periodEnd;

	public ExceptionPeriod(Date periodBegin, Date periodEnd) {
		this.periodBegin = periodBegin;
		this.periodEnd = periodEnd;
	}

	public Date getPeriodBegin() {
		return periodBegin;
	}
	
	public Date getPeriodEnd() {
		return periodEnd;
	}
}
