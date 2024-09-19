package com.journaling.journalApp.serviceImpl;

import com.journaling.journalApp.entity.JournalEntry;
import com.journaling.journalApp.services.AudioService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class AudioServiceImpl implements AudioService {

    @Value("${audio-API-Key}") private String API_KEY ;
    @Value("${audio-voice-ID}") private String VOICE_ID;
    @Value("${audio-API-Url}") private String ELEVENLABS_API_URL; // Injected API URL


    private final RestTemplate restTemplate;

    public AudioServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String convertTextToSpeech(String text, String fileName) {
        // Replace placeholder in the API URL

        String apiUrl = ELEVENLABS_API_URL.replace("{VOICE_ID}", VOICE_ID);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("xi-api-key", API_KEY);
            headers.set("Content-Type", "application/json");

            String requestBody = "{ \"text\": \"" + text + "\" }";
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<byte[]> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, byte[].class);
            byte[] audioData = response.getBody();

            if (audioData != null) {
                return saveAudioFile(audioData, fileName);
            } else {
                throw new RuntimeException("No audio data received.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error in text-to-speech conversion: " + e.getMessage(), e);
        }
    }


    private String saveAudioFile(byte[] audioData, String fileName) {
        try {
            Path directory = Paths.get("audio");
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            // Create file path with the custom file name
            Path filePath = directory.resolve(fileName + ".mp3");
            Files.write(filePath, audioData);

            return filePath.toUri().toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save audio file.", e);
        }
    }

    @Transactional
    public String replaceAudioFile(JournalEntry entry, String newText) {
        // Convert the audio URL from URI to Path
        Path existingFilePath = null;
        try {
            URI audioUri = new URI(entry.getAudioUrl());
            existingFilePath = Paths.get(audioUri);
        } catch (URISyntaxException e) {
            System.err.println("Invalid URI syntax for existing audio file: " + e.getMessage());
        }

        // Delete the existing audio file if it exists
        if (existingFilePath != null && Files.exists(existingFilePath)) {
            try {
                Files.delete(existingFilePath);
            } catch (IOException e) {
                System.err.println("Failed to delete old audio file: " + e.getMessage());
            }
        }

        String fileName = generateFileName(entry); // Generate the custom file name
        String newAudioUrl = convertTextToSpeech(newText, fileName); // Pass the fileName to the method
        entry.setAudioUrl(newAudioUrl);
        return newAudioUrl;
    }

    public String generateFileName(JournalEntry entry) {
        String userName = entry.getOwner();
        String title = entry.getTitle().replaceAll("[^a-zA-Z0-9\\-]", "-");
        long timestamp = System.currentTimeMillis(); // Add a timestamp for uniqueness
        return userName + "-" + title + "-" + timestamp;
    }

    private String getFilePathFromUrl(String audioUrl) {
        // Extract file path from URL
        return Paths.get(URI.create(audioUrl)).toString();
    }

    @Transactional
    public void deleteAudioFile(String audioUrl) {
        try {
            Path audioFilePath = Paths.get(new URI(audioUrl));
            if (Files.exists(audioFilePath)) {
                Files.delete(audioFilePath); // Delete the audio file from the system
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Failed to delete audio file: " + e.getMessage(), e);
        }
    }

    public void renameAudioFile(String oldAudioUrl, String newFileName) throws IOException {
        Path oldFilePath;
        try {
            URI oldFileUri = new URI(oldAudioUrl);
            oldFilePath = Paths.get(oldFileUri);
        } catch (URISyntaxException e) {
            throw new IOException("Invalid URI syntax for old audio file URL: " + oldAudioUrl, e);
        }

        Path newFilePath = Paths.get("audio").resolve(newFileName);

        if (Files.exists(oldFilePath)) {
            Files.move(oldFilePath, newFilePath); // Rename the file
        } else {
            throw new IOException("Old audio file does not exist.");
        }
    }



}
