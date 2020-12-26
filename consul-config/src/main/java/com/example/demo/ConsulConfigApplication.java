package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling    // 用于定时拉取配置
@SpringBootApplication
public class ConsulConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsulConfigApplication.class, args);
	}

}
