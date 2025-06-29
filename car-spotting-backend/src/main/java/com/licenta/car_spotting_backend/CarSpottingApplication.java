package com.licenta.car_spotting_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
@SpringBootApplication
public class CarSpottingApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarSpottingApplication.class, args);
	}

}
