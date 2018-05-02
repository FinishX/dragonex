package com.example.demo;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@ImportResource(locations={"classpath:spring-redis.xml"})
@SpringBootApplication
public class DragonexApplication {

	public static void main(String[] args) {
		SpringApplication.run(DragonexApplication.class, args);
	}
}
