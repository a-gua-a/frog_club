package com.frog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@Slf4j
@EnableConfigurationProperties
public class FrogClubApplication {

	public static void main(String[] args) {

		SpringApplication.run(FrogClubApplication.class, args);
		log.info("FrogClubApplication started");
	}

}
