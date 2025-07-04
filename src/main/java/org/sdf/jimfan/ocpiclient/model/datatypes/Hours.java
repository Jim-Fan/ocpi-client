package org.sdf.jimfan.ocpiclient.model.datatypes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Hours {

	@JsonProperty("twentyfourseven")
	private Boolean twentyFourSeven;

	@JsonProperty("regular_hours")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<RegularHours> regularHours;
	
	@JsonProperty("exceptional_openings")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<ExceptionPeriod> exceptionalOpenings;
	
	@JsonProperty("exceptional_closings")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<ExceptionPeriod> exceptionalClosings;

	public Hours(Boolean twentyFourSeven, List<RegularHours> regularHours, List<ExceptionPeriod> exceptionalOpenings, List<ExceptionPeriod> exceptionalClosings) {
		this.twentyFourSeven = twentyFourSeven;
		this.regularHours = regularHours;
		this.exceptionalOpenings = exceptionalOpenings;
		this.exceptionalClosings = exceptionalClosings;
	}

	public Boolean getTwentyFourSeven() {
		return twentyFourSeven;
	}
	
	public List<RegularHours> getRegularHours() {
		return regularHours;
	}
	
	public List<ExceptionPeriod> getExceptionalOpenings() {
		return exceptionalOpenings;
	}

	public List<ExceptionPeriod> getExceptionalClosings() {
		return exceptionalClosings;
	}
}
