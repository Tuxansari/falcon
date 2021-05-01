package com.fameoflight.falcon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableCircuitBreaker
@SpringBootApplication
@EnableJpaRepositories
@EnableFeignClients(basePackages = "com.fameoflight.falcon.client")
public class FalconApplication {

	public static void main(String[] args) {
		SpringApplication.run(FalconApplication.class, args);
	}

}
