package com.devicesimulator.thingsboard_device_simulator.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.thingsboard.rest.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class ThingsboardConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThingsboardConfig.class);
    public static final String BASE_URL = "http://localhost:8080/api";
    public static final String USERNAME = "tenant@thingsboard.org";
    public static final String PASSWORD = "tenant";

    @Value("${thingsboard.server.url}")
    private String thingsboardUrl;

    @Value("${tenant.username}")
    private String tenantUsername;

    @Value("${tenant.password}")
    private String tenantPassword;

    @Bean
    public RestClient restClient() {
        return new RestClient(thingsboardUrl);
    }

    @Bean("tenantRestClient")
    public RestClient tenantRestClient(RestClient restClient) {
        try {
            restClient.login(tenantUsername, tenantPassword);
            LOGGER.info("Successfully logged in to ThingsBoard!");
        } catch (Exception e) {
            LOGGER.error("Failed to login to ThingsBoard: {}", e.getMessage());
            throw new RuntimeException("ThingsBoard login failed!", e);
        }
        return restClient;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


}

