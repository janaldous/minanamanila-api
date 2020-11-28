package com.janaldous.minanamanila;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class MinanaManilaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MinanaManilaApplication.class, args);
	}

}
