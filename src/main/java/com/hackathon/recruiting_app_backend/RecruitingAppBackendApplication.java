package com.hackathon.recruiting_app_backend;

import com.hackathon.recruiting_app_backend.config.EnvConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RecruitingAppBackendApplication {
    public static void main(String[] args) {
//        SpringApplication.run(RecruitingAppBackendApplication.class, args);
        SpringApplication app = new SpringApplication(RecruitingAppBackendApplication.class);
        app.addInitializers(new EnvConfig());
        app.run(args);
    }
}
