package com.example.studentDetailsBackEnd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.studentDetailsBackEnd")
public class StudentDetailsBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentDetailsBackEndApplication.class, args);
	}

}
