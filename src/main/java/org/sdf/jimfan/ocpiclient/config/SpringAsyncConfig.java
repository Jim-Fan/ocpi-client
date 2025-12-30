package org.sdf.jimfan.ocpiclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class SpringAsyncConfig {

	@Bean("ocpiTaskExecutor")
	SimpleAsyncTaskExecutor ocpiTaskExecutor() {
		return new SimpleAsyncTaskExecutor("ocpi-task-executor-");
	}
}
