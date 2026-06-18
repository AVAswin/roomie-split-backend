package com.roomiesplit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling; // for Daily Remainder

// @EnableScheduling
@SpringBootApplication
public class RoomieSplitApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoomieSplitApplication.class, args);
	}

}
