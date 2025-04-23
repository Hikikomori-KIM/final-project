package com.hiki.academyfinal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class AcademyfinalApplication {

	public static void main(String[] args) {
		SpringApplication.run(AcademyfinalApplication.class, args);
	}

}
