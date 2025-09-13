package org.sdf.jimfan.ocpiclient.exception;

import java.util.Date;

import org.sdf.jimfan.ocpiclient.model.OcpiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Reference:
 * 
 * [1] https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc
 * 
 * [2] https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-exceptionhandler.html#mvc-ann-exceptionhandler-args
 */
@RestControllerAdvice
public class OcpiControllerAdvice {

	private final Logger logger = LoggerFactory.getLogger(OcpiControllerAdvice.class);
	
	/**
	 * Generic, catch-all exception handler
	 */
	@ExceptionHandler(RuntimeException.class)
	public OcpiResponse<String> runtimeExceptionHandler(RuntimeException ex, HttpServletRequest request, HttpServletResponse response) {
		
		logger.error(ex.getMessage());
		ex.printStackTrace();
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		OcpiResponse<String> ocpiResponse = new OcpiResponse<String>(null, 3000, "Unexpected error, please contact system administrator", new Date());
		return ocpiResponse;
	}
}
