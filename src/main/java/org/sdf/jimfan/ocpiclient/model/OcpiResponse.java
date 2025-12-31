package org.sdf.jimfan.ocpiclient.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;

public class OcpiResponse<T> extends JsonFormattable {

	@JsonProperty("data")
	@JsonIgnoreProperties(ignoreUnknown = true) // TODO: Review this
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private T data;
	
	@JsonProperty("status_code")
	private int statusCode;
	
	@JsonProperty("status_message")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String statusMessage;
	
	@JsonProperty("timestamp")
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
	private ZonedDateTime timestamp;
	
	public OcpiResponse(T data, int statusCode, String statusMessage, ZonedDateTime timestamp) {
		this.data = data;
		this.statusCode = statusCode;
		this.statusMessage = statusMessage;
		this.timestamp = timestamp;
	}
	
	public T getData() {
		return data;
	}
	
	public int getStatusCode() {
		return statusCode;
	}
	
	public String getStatusMessage() {
		return statusMessage;
	}
	
	public ZonedDateTime getTimestamp() {
		return timestamp;
	}
}
