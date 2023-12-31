package com.project.micro.movementcounts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class MovementcountsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovementcountsApplication.class, args);
	}

}
