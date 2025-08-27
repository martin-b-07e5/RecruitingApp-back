package com.hackathon.recruiting_app_backend;

import com.hackathon.recruiting_app_backend.model.Candidate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RecruitingAppBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecruitingAppBackendApplication.class, args);
		System.out.println("Hello World From HireConnect App");

//		Candidate candidate = Candidate.builder()
//				.email("test@example.com")
//				.password("secure123")
//				.firstName("Ana")
//				.lastName("Gomez")
//				.phone("123456789")
//				.skills("Java, Spring Boot")
//				.build();
//
//		System.out.println(candidate);

	}
}
