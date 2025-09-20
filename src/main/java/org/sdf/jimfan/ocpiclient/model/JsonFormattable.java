package org.sdf.jimfan.ocpiclient.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class JsonFormattable {

	// This variable is accessed by _all_ instances of _all_ sub-classes, and
	// can be bad for performance.
	// private static ObjectMapper mapper = new ObjectMapper();
	
	// https://www.baeldung.com/java-threadlocal
	private static final ThreadLocal<ObjectMapper> mapperThreadLocal =
			ThreadLocal.withInitial(() -> {
				ObjectMapper mapper = new ObjectMapper();
				mapper.registerModule(new JavaTimeModule());
				return mapper;
			});
	
	@Override
	public String toString() {
		String result = null;
		try {
			result = mapperThreadLocal.get().writeValueAsString(this);
		}
		catch (JsonProcessingException ex) {
			result = super.toString();
		}
		return result;
	}
}
