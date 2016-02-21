package com.github.dtrunk90.thymeleaf.jawr.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.dtrunk90.thymeleaf.jawr.dialect.JawrDialect;

@Configuration
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class JawrThymeleafAutoConfiguration {
	@Bean
	public JawrDialect jawrDialect() {
		return new JawrDialect();
	}
}