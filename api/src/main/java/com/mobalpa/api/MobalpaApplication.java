package com.mobalpa.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MobalpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MobalpaApplication.class, args);
	}

}
