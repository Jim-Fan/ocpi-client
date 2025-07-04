package org.sdf.jimfan.ocpiclient.model.datatype;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DisplayText {

	@JsonProperty("language")
	private LanguageCode language;
	
	@JsonProperty("text")
	private String text;

	public DisplayText(LanguageCode language, String text) {
		this.language = language;
		this.text = text;
	}

	public LanguageCode getLanguage() {
		return language;
	}

	public String getText() {
		return text;
	}
}
