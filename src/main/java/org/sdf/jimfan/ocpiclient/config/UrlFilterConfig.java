package org.sdf.jimfan.ocpiclient.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.filter.UrlHandlerFilter;


/**
 * Reference: https://www.baeldung.com/spring-boot-3-url-matching
 */
@Configuration
public class UrlFilterConfig {

	/**
	 * Certain MSP implementation adds trailing slash to URL in their OCPI requests. Handle it by trimming slash.
	 */
	@Bean
    FilterRegistrationBean<OncePerRequestFilter> urlHandlerFilterRegistrationBean() {
		
		FilterRegistrationBean<OncePerRequestFilter> registrationBean = new FilterRegistrationBean<>();
		UrlHandlerFilter urlHandlerFilter = UrlHandlerFilter
				.trailingSlashHandler("/ocpi/**").wrapRequest()
				.build();
		registrationBean.setFilter(urlHandlerFilter);

		return registrationBean;
    }
}
