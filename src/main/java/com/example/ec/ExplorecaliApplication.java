package com.example.ec;

import com.example.ec.service.TourPackageService;
import com.example.ec.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExplorecaliApplication {
	@Autowired
	private TourPackageService tourPackageService;

	@Autowired
	private TourService tourService;

	public static void main(String[] args) {
		SpringApplication.run(ExplorecaliApplication.class, args);
	}

}
