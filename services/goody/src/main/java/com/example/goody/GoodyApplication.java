package com.example.goody;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.TimeZone;

@SpringBootApplication
@EnableFeignClients
public class GoodyApplication {

	public static void main(String[] args) {

		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		System.setProperty("user.timezone", "UTC");

		SpringApplication.run(GoodyApplication.class, args);
	}

	@PostConstruct
	public void logTimezone() {
		System.out.println("=== JVM timezone (TimeZone.getDefault()) = " + TimeZone.getDefault().getID());
		System.out.println("=== System property user.timezone = " + System.getProperty("user.timezone"));
	}

}
