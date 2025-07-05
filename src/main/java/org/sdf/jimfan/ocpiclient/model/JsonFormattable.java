package org.sdf.jimfan.ocpiclient.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class JsonFormattable {

	private static ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public String toString() {
		String result = null;
		try {
			result = mapper.writeValueAsString(this);
		}
		catch (JsonProcessingException ex) {
			result = super.toString();
		}
		return result;
	}
}
