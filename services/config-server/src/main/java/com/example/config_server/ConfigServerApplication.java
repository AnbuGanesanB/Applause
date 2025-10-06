package com.example.config_server;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

import java.util.TimeZone;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {

	public static void main(String[] args) {

		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		System.setProperty("user.timezone", "UTC");

		SpringApplication.run(ConfigServerApplication.class, args);
	}

	@PostConstruct
	public void logTimezone() {
		System.out.println("=== JVM timezone (TimeZone.getDefault()) = " + TimeZone.getDefault().getID());
		System.out.println("=== System property user.timezone = " + System.getProperty("user.timezone"));
	}

}
