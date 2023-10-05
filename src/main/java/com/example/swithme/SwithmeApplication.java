package com.example.swithme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@PropertySource("classpath:application.properties")
public class SwithmeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SwithmeApplication.class, args);
	}

}
