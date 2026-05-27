package com.strivolabs.strivolabsassessmentjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class StrivoLabsAssessmentJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(StrivoLabsAssessmentJavaApplication.class, args);
	}

}
