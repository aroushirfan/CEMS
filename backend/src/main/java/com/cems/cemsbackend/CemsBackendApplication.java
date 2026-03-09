package com.cems.cemsbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CemsBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(CemsBackendApplication.class, args);
	}
}