package com.devicesimulator.thingsboard_device_simulator.service;

import com.devicesimulator.thingsboard_device_simulator.config.ThingsboardConfig;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RestTemplate restTemplate;

    public AuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getJwtToken() {
        String url = ThingsboardConfig.BASE_URL + "/auth/login";

        Map<String, String> body = new HashMap<>();
        body.put("username", ThingsboardConfig.USERNAME);
        body.put("password", ThingsboardConfig.PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.POST, request, JsonNode.class);
        return response.getBody().get("token").asText();
    }
}