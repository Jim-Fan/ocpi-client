package org.sdf.jimfan.ocpiclient.model.command;

import org.sdf.jimfan.ocpiclient.model.JsonFormattable;
import org.sdf.jimfan.ocpiclient.model.datatype.CommandResultType;
import org.sdf.jimfan.ocpiclient.model.datatype.DisplayText;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class CommandResult extends JsonFormattable {

	@JsonProperty("result")
	private CommandResultType result;
	
	@JsonProperty("message")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<DisplayText> message;

	public CommandResult(CommandResultType result, List<DisplayText> message) {
		this.result = result;
		this.message = message;
	}

	public CommandResultType getResult() {
		return result;
	}

	public void setResult(CommandResultType result) {
		this.result = result;
	}

	public List<DisplayText> getMessage() {
		return message;
	}

	public void setMessage(List<DisplayText> message) {
		this.message = message;
	}
}
