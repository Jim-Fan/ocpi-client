package org.sdf.jimfan.ocpiclient.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ControllerInterceptor implements HandlerInterceptor {

	private final Logger logger = LoggerFactory.getLogger(ControllerInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		logger.info("{} {}", request.getMethod(), request.getRequestURI());
		return true;
	}
}
