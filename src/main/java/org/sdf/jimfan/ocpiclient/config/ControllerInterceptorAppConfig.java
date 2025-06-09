package org.sdf.jimfan.ocpiclient.config;

import org.sdf.jimfan.ocpiclient.interceptor.ControllerInterceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class ControllerInterceptorAppConfig implements WebMvcConfigurer {

	@Autowired
	private ControllerInterceptor controllerInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(this.controllerInterceptor);
	}
}
