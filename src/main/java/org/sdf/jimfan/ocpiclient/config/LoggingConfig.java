package org.sdf.jimfan.ocpiclient.config;

import org.sdf.jimfan.ocpiclient.interceptor.RequestLoggingInterceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class LoggingConfig implements WebMvcConfigurer {

	@Autowired
	private RequestLoggingInterceptor controllerInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(this.controllerInterceptor);
	}
}
