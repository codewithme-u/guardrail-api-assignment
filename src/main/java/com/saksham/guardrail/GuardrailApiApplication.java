package com.saksham.guardrail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GuardrailApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuardrailApiApplication.class, args);
	}

}
