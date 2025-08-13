package com.one;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OneApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(OneApiApplication.class, args);
	}

}
