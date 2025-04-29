package com.journaling.journalApp.services;

import com.journaling.journalApp.models.EmotionAnalysisResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmotionService {

    private static final String API_URL = "http://127.0.0.1:8000/analyze-emotion"; // FastAPI endpoint

    // Method to sanitize the content by removing control characters
    public String sanitizeContent(String input) {
        // Remove control characters, zero-width spaces, and other non-printable characters
        return input.replaceAll("[\\p{Cntrl}\\u200B\\u200C\\u200D\\u200E\\u200F\\u202A\\u202B\\u202C\\u202D\\u202E\\u2060\\uFEFF]", "");
    }




    public EmotionAnalysisResponse analyzeEmotion(String text) {
        // Sanitize the text content before sending it to FastAPI
        String sanitizedText = sanitizeContent(text);

        // Log the sanitized text for debugging
        System.out.println("Sanitized Text: " + sanitizedText);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // JSON body with sanitized text input
        String requestBody = "{\"text\": \"" + sanitizedText + "\"}";

        // Log the request body for debugging
        System.out.println("Request Body: " + requestBody);

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // Send POST request to FastAPI and map response to EmotionAnalysisResponse
        ResponseEntity<EmotionAnalysisResponse> response = restTemplate.postForEntity(API_URL, request, EmotionAnalysisResponse.class);

        // Return the response body (which is of type EmotionAnalysisResponse)
        return response.getBody();
    }
}