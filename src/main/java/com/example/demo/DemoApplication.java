package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.example.demo.controllers.DemoLogbookController;
import com.example.demo.services.impl.DemoServiceImpl;

@SpringBootApplication
@ComponentScan(basePackageClasses = {DemoServiceImpl.class, DemoLogbookController.class})
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
