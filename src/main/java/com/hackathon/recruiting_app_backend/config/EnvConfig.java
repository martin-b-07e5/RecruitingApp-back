package com.hackathon.recruiting_app_backend.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

public class EnvConfig implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        try {
            // Try to load .env file (fallback if no system env vars are set)
            Dotenv dotenv = Dotenv.configure()
                    .directory("./")
                    .filename(".env")
                    .ignoreIfMissing()
                    .load();

            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            Map<String, Object> envMap = new HashMap<>();

            // Add all .env variables to Spring environment, but system env vars take precedence
            dotenv.entries().forEach(entry -> {
                // Only add if not already set as system environment variable
                if (System.getenv(entry.getKey()) == null) {
                    envMap.put(entry.getKey(), entry.getValue());
                }
            });

            // Also add system environment variables
            System.getenv().forEach(envMap::put);

            environment.getPropertySources().addFirst(new MapPropertySource("environment", envMap));

            System.out.println("Environment variables loaded successfully");
            System.out.println("Database host: " + envMap.get("DB_HOST"));
        } catch (Exception e) {
            System.err.println("Failed to load environment variables: " + e.getMessage());
        }
    }
}