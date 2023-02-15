package com.example.ec;

import com.example.ec.service.TourRatingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Main Class for the Spring Boot Application
 *
 * Created by Mary Ellen Bowman
 */
@SpringBootApplication
public class ExplorecaliApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(TourRatingService.class);

	public static void main(String[] args) {
		LOGGER.info("ExplorecaliApplication entering");
		//BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
		//String encodedPasswd = encoder.encode("letmein");
		//System.out.println("encoded passwd [" + encodedPasswd + "]");
		SpringApplication.run(ExplorecaliApplication.class, args);
	}

}
