package com.janaldous.minanamanila;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.core.env.Environment;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class MinanaManilaApplication {
	
	private Logger logger = LoggerFactory.getLogger(MinanaManilaApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MinanaManilaApplication.class, args);
	}
	
	@Autowired
    private Environment environment;
	
	@PostConstruct
    public void init() {
		logger.info("This is the port: " + environment.getProperty("PORT"));
    }

}
