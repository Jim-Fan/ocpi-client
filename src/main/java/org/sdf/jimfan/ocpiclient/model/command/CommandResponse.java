package org.sdf.jimfan.ocpiclient.model.command;


import org.sdf.jimfan.ocpiclient.model.datatype.CommandResponseType;
import org.sdf.jimfan.ocpiclient.model.datatype.DisplayText;

import java.util.List;

import org.sdf.jimfan.ocpiclient.model.JsonFormattable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CommandResponse extends JsonFormattable {

	@JsonProperty("result")
	private CommandResponseType result;
	
	@JsonProperty("timeout")
	private Integer timeout;
	
	@JsonProperty("message")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<DisplayText> message;
	
	public CommandResponse(CommandResponseType result, Integer timeout, List<DisplayText> message) {
		this.result = result;
		this.timeout = timeout;
		this.message = message;
	}
	
	public CommandResponse(CommandResponseType result, Integer timeout) {
		this(result, timeout, null);
	}

	public CommandResponseType getResult() {
		return result;
	}

	public void setResult(CommandResponseType result) {
		this.result = result;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public List<DisplayText> getMessage() {
		return message;
	}

	public void setMessage(List<DisplayText> message) {
		this.message = message;
	}
}
