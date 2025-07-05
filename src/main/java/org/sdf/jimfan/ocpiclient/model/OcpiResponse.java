package org.sdf.jimfan.ocpiclient.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

public class OcpiResponse extends JsonFormattable {

	@JsonProperty("data")
	@JsonIgnoreProperties(ignoreUnknown = true) 
	private Object data;
	
	@JsonProperty("status_code")
	private int statusCode;
	
	@JsonProperty("status_message")
	private String statusMessage;
	
	@JsonProperty("timestamp")
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
	private Date timestamp;
	
	public OcpiResponse(Object data, int statusCode, String statusMessage, Date timestamp) {
		this.data = data;
		this.statusCode = statusCode;
		this.statusMessage = statusMessage;
		this.timestamp = timestamp;
	}
	
	public Object getData() {
		return data;
	}
	
	public int getStatusCode() {
		return statusCode;
	}
	
	public String getStatusMessage() {
		return statusMessage;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
}
