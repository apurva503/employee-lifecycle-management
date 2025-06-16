package com.pepsico.hcmservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class HcmServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HcmServiceApplication.class, args);
	}

}
