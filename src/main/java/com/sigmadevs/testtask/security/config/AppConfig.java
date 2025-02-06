package com.sigmadevs.testtask.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    @Value("${spring.security.front-end-url}")
    private String cors;


    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins(cors).
                        allowedOriginPatterns("*").exposedHeaders("*", "Set-Cookie", "X-XSRF-TOKEN")
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
                        .allowCredentials(true);
            }
        };
    }
}
