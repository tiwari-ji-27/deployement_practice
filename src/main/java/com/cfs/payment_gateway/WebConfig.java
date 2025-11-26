package com.cfs.payment_gateway;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Global CORS Configuration to resolve the OPTIONS 403 Forbidden error.
 * This ensures CORS headers are applied early in the request lifecycle for all API endpoints.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Apply CORS mapping to all API endpoints under /api
        registry.addMapping("/api/**")
                // Allow requests from any origin (for development)
                .allowedOrigins("*")
                // Allow necessary HTTP methods including OPTIONS for preflight requests
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false)
                .maxAge(3600); // Cache preflight response for 1 hour
    }
}
