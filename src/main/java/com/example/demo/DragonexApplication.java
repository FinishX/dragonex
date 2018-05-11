package com.example.demo;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@ImportResource(locations={"classpath:spring-redis.xml"})
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class DragonexApplication{

	public static void main(String[] args) {
		SpringApplication.run(DragonexApplication.class, args);
	}
	
}
