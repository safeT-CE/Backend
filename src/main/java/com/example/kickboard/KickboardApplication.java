package com.example.kickboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KickboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(KickboardApplication.class, args);
	}

}
