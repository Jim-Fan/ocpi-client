package org.sdf.jimfan.ocpiclient.model.tariff;

import org.sdf.jimfan.ocpiclient.model.JsonFormattable;
import org.sdf.jimfan.ocpiclient.model.datatype.ReservationRestrictionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.util.List;

public class TariffRestrictions extends JsonFormattable {

	@JsonProperty("start_time")
	@JsonFormat(pattern = "HH:mm")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private LocalTime startTime;
	
	@JsonProperty("end_time")
	@JsonFormat(pattern = "HH:mm")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private LocalTime endTime;
	
	@JsonProperty("start_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private LocalDate startDate;
	
	@JsonProperty("end_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private LocalDate endDate;
	
	@JsonProperty("min_kwh")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Float minEnergyConsumed;
	
	@JsonProperty("max_kwh")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Float maxEnergyConsumed;
	
	@JsonProperty("min_current")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Float minCurrent;
	
	@JsonProperty("max_current")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Float maxCurrent;
	
	@JsonProperty("min_power")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Float minPower;
	
	@JsonProperty("max_power")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Float maxPower;
	
	@JsonProperty("min_duration")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer minDurationInSecond;
	
	@JsonProperty("max_duration")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer maxDurationInSecond;
	
	@JsonProperty("day_of_week")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<DayOfWeek> dayOfWeek;
	
	@JsonProperty("reservation")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private ReservationRestrictionType reservationRestrictionType;

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public Float getMinEnergyConsumed() {
		return minEnergyConsumed;
	}

	public void setMinEnergyConsumed(Float minEnergyConsumed) {
		this.minEnergyConsumed = minEnergyConsumed;
	}

	public Float getMaxEnergyConsumed() {
		return maxEnergyConsumed;
	}

	public void setMaxEnergyConsumed(Float maxEnergyConsumed) {
		this.maxEnergyConsumed = maxEnergyConsumed;
	}

	public Float getMinCurrent() {
		return minCurrent;
	}

	public void setMinCurrent(Float minCurrent) {
		this.minCurrent = minCurrent;
	}

	public Float getMaxCurrent() {
		return maxCurrent;
	}

	public void setMaxCurrent(Float maxCurrent) {
		this.maxCurrent = maxCurrent;
	}

	public Float getMinPower() {
		return minPower;
	}

	public void setMinPower(Float minPower) {
		this.minPower = minPower;
	}

	public Float getMaxPower() {
		return maxPower;
	}

	public void setMaxPower(Float maxPower) {
		this.maxPower = maxPower;
	}

	public Integer getMinDurationInSecond() {
		return minDurationInSecond;
	}

	public void setMinDurationInSecond(Integer minDurationInSecond) {
		this.minDurationInSecond = minDurationInSecond;
	}

	public Integer getMaxDurationInSecond() {
		return maxDurationInSecond;
	}

	public void setMaxDurationInSecond(Integer maxDurationInSecond) {
		this.maxDurationInSecond = maxDurationInSecond;
	}

	public List<DayOfWeek> getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(List<DayOfWeek> dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public ReservationRestrictionType getReservationRestrictionType() {
		return reservationRestrictionType;
	}

	public void setReservationRestrictionType(ReservationRestrictionType reservationRestrictionType) {
		this.reservationRestrictionType = reservationRestrictionType;
	}
}
