package com.hackathon.recruiting_app_backend;

import com.hackathon.recruiting_app_backend.model.Candidate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RecruitingAppBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecruitingAppBackendApplication.class, args);
		System.out.println("Hello World From HireConnect App");
	}
}
