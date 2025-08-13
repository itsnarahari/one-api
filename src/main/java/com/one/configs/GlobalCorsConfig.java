package com.one.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GlobalCorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Apply to all endpoints
                        .allowedOrigins("*") // Allow all origins
                        .allowedMethods("*") // Allow all HTTP methods: GET, POST, PUT, DELETE, etc.
                        .allowedHeaders("*") // Allow all headers
                        .allowCredentials(false) // Don't allow credentials (cookies, etc.)
                        .maxAge(3600); // Cache preflight response for 1 hour
            }
        };
    }
}

