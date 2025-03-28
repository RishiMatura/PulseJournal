//package com.journaling.journalApp.services;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import java.util.HashMap;
//import java.util.Map;
//
//@Slf4j
//@Service
//public class ResendEmailService {
//
//    @Value("${resend.api.key}")  // Load API Key from application.properties
//    private String apiKey;
//
//    private static final String RESEND_API_URL = "https://api.resend.com/emails";
//
//    public void sendEmail(String to, String subject, String htmlContent) {
//        try {
//            RestTemplate restTemplate = new RestTemplate();
//
//            // Set headers
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            headers.setBearerAuth(apiKey);  // Using API Key for Authorization
//
//            // Create request body
//            Map<String, Object> requestBody = new HashMap<>();
//            requestBody.put("from", "onboarding@resend.dev");
//            requestBody.put("to", new String[]{to});
//            requestBody.put("subject", subject);
//            requestBody.put("html", htmlContent);
//
//            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
//
//            // Send POST request to Resend API
//            String response = restTemplate.postForObject(RESEND_API_URL, requestEntity, String.class);
//
//            log.info("Email sent successfully: {}", response);
//        } catch (Exception e) {
//            log.error("Error sending email via Resend API", e);
//        }
//    }
//}
