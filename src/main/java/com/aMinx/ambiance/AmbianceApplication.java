package com.aMinx.ambiance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class AmbianceApplication extends SpringBootServletInitializer {

	//extends SpringBootServletInitializer
	public static void main(String[] args) {
		SpringApplication.run(AmbianceApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(AmbianceApplication.class);
	}

}
