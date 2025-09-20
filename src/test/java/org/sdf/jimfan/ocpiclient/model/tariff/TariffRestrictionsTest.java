package org.sdf.jimfan.ocpiclient.model.tariff;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.time.LocalTime;

public class TariffRestrictionsTest {

	private static final Logger logger = LoggerFactory.getLogger(TariffRestrictionsTest.class);
	private static final ObjectMapper mapper = new ObjectMapper();
	
	@BeforeAll
	public static void init() {
		mapper.registerModule(new JavaTimeModule());
	}
	
	@Test
	public void objectMapper_should_notThrowWhenGivenCorrectJson() {
		
		String json = "{\"start_time\":\"12:34\",\"end_time\":\"19:37\",\"start_date\":\"2025-09-20\",\"end_date\":\"2046-06-30\"}";
		logger.info("Using JSON string = {}", json);
		
		Assertions.assertDoesNotThrow(() -> {
			mapper.readValue(json, TariffRestrictions.class);
		});
	}
	
	@Test
	public void objectMapper_should_parseJsonToCorrectObjectValues() {
		
		String json = "{\"start_time\":\"12:34\",\"end_time\":\"19:37\",\"start_date\":\"2025-09-20\",\"end_date\":\"2046-06-30\"}";
		logger.info("Using JSON string = {}", json);
		
		TariffRestrictions tr = null;
		try {
			tr = mapper.readValue(json, TariffRestrictions.class);
		}
		catch (JsonProcessingException processingEx) {
			logger.error(processingEx.getMessage());
		}
		
		Assertions.assertNotNull(tr);
		
		Assertions.assertEquals(12, tr.getStartTime().getHour());
		Assertions.assertEquals(34, tr.getStartTime().getMinute());
		Assertions.assertEquals(19, tr.getEndTime().getHour());
		Assertions.assertEquals(37, tr.getEndTime().getMinute());
		
		Assertions.assertEquals(2025, tr.getStartDate().getYear());
		Assertions.assertEquals(9, tr.getStartDate().getMonth().getValue());
		Assertions.assertEquals(20, tr.getStartDate().getDayOfMonth());

		Assertions.assertEquals(2046, tr.getEndDate().getYear());
		Assertions.assertEquals(6, tr.getEndDate().getMonth().getValue());
		Assertions.assertEquals(30, tr.getEndDate().getDayOfMonth());
	}
	
	@Test
	public void toString_should_returnCorrectStartDate() {
		
		TariffRestrictions tr = new TariffRestrictions();
		tr.setStartDate(LocalDate.of(2025, 9, 20));
		String json = tr.toString();
		logger.info("JSON = {}", json);
		Assertions.assertTrue(json.contains("\"start_date\":\"2025-09-20\""));
	}
	
	@Test
	public void toString_should_returnCorrectStartTime() {
		
		TariffRestrictions tr = new TariffRestrictions();
		tr.setStartTime(LocalTime.of(21, 0));
		String json = tr.toString();
		logger.info("JSON = {}", json);
		Assertions.assertTrue(json.contains("\"start_time\":\"21:00\""));
	}
}
