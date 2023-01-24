package com.reesen.Reesen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
public class ReesenApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReesenApplication.class, args);
	}

}
