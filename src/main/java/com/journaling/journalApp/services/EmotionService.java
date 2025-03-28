package com.journaling.journalApp.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmotionService {
    private static final String API_URL = "http://127.0.0.1:8000/analyze-emotion"; // FastAPI endpoint

    public String analyzeEmotion(String text) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // JSON body with text input
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("text", text);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        // Send POST request to FastAPI
        ResponseEntity<Map> response = restTemplate.postForEntity(API_URL, request, Map.class);

        // Extract emotion label from response
        if (response.getBody() != null && response.getBody().containsKey("label")) {
            return response.getBody().get("label").toString();
        }
        return "UNKNOWN"; // Default if no emotion detected
    }
}

