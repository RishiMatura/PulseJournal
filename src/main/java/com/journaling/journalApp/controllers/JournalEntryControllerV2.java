package com.journaling.journalApp.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.journaling.journalApp.entity.JournalEntry;
import com.journaling.journalApp.entity.User;
import com.journaling.journalApp.enums.Sentiment;
import com.journaling.journalApp.models.EmotionAnalysisResponse;
import com.journaling.journalApp.serviceImpl.AudioServiceImpl;
import com.journaling.journalApp.services.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;
@CrossOrigin(origins = "http://localhost:3000")  // Allow requests from React frontend
@RestController
@RequestMapping("/journal")             // maps this whole class to this endpoint
//    All the controllers that are used in the project are stored in this directory

public class JournalEntryControllerV2 {
    @Autowired          private JournalEntryService journalEntryService;
    @Autowired          private UserService userService;
    @Autowired          private RestTemplate restTemplate;
    @Autowired          private AudioService audioService;
    @Autowired          private EmotionService emotionService; // New Service for Emotion Analysis
    @Autowired          private SentimentAnalysisService sentimentAnalysisService;


    @GetMapping("/public/auth-check")
    public ResponseEntity<String> checkAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return ResponseEntity.ok("Authenticated as: " + authentication.getName());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    //    1)    Fetch all the entries
    @GetMapping("/getAll")     // localhost:8080/journal/get-ALl
    public ResponseEntity<?> getAllJournalEntriesOfUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        User user = userService.findByUserName(userName);
        List<JournalEntry> all = user.getJournalEntries();
        if(all != null && !all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND) ;
    }

    //    2)    Create an Entry
    @PostMapping("/post")
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry myEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        // Basic validation to avoid null or blank title/content
        if (myEntry.getTitle() == null || myEntry.getTitle().trim().isEmpty() ||
                myEntry.getContent() == null || myEntry.getContent().trim().isEmpty()) {
            return new ResponseEntity<>("Title and Content cannot be empty.", HttpStatus.BAD_REQUEST);
        }

        try {
            // Sanitize the title and content
            String sanitizedTitle = emotionService.sanitizeContent(myEntry.getTitle());
            String sanitizedContent = emotionService.sanitizeContent(myEntry.getContent());

            // Set owner
            myEntry.setOwner(userName);

            // Call to the emotion analysis service and get all emotions
            // Updated to fetch the top emotion and all emotions
            EmotionAnalysisResponse emotionResponse = emotionService.analyzeEmotion(sanitizedTitle + " " + sanitizedContent);

            // Set the top emotion and all emotions
            myEntry.setTopEmotion(emotionResponse.getTop_emotion());  // Assuming EmotionAnalysisResponse has getTopEmotion()
            myEntry.setAllEmotions(emotionResponse.getAll_emotions());  // Assuming EmotionAnalysisResponse has getAllEmotions()
            myEntry.setEmotion(emotionResponse.getTop_emotion().getLabel());  // Add top emotion label to `emotion`

            // Sentiment analysis
            Sentiment sentiment = sentimentAnalysisService.getSentiment(sanitizedTitle + " " + sanitizedContent);
            myEntry.setSentiment(sentiment);

            // Save entry
            journalEntryService.saveEntry(myEntry, userName);

            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
            return new ResponseEntity<>("Something went wrong while creating entry.", HttpStatus.BAD_REQUEST);
        }
    }




//   3)     Get Entry By ID
    @GetMapping("/id/{myID}")       // localhost:8080/journal/id/{myID}
    public ResponseEntity<JournalEntry> getEntryByID(@PathVariable ObjectId myID){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream()
                .filter(x -> x.getId().equals(myID))
                .collect(Collectors.toList());
        if(!collect.isEmpty()){
            Optional<JournalEntry> journalEntry = journalEntryService.findById(myID);
            if(journalEntry.isPresent()){
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
            }
        }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//   4)     Delete Entry By ID
@DeleteMapping("/id/{myID}")
public ResponseEntity<?> deleteEntryByID(@PathVariable ObjectId myID) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userName = authentication.getName();

    // Fetch the journal entry by its ID
    Optional<JournalEntry> journalEntry = journalEntryService.findById(myID);

    if (journalEntry.isPresent()) {
        JournalEntry entry = journalEntry.get();

        // Check if the user is the owner of the entry
        if (entry.getOwner().equals(userName)) {
            // Delete the associated audio file if it exists
//            String audioUrl = entry.getAudioUrl();
//            if (audioUrl != null && !audioUrl.isEmpty()) {
//                audioService.deleteAudioFile(audioUrl);  // Method to delete audio file
//            }
            // Delete the journal entry from the database
            journalEntryService.deleteById(myID, userName);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // User is not allowed to delete this entry
        }
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
}


    @PutMapping("id/{myID}")
    public ResponseEntity<?> updateJournalByID(@PathVariable ObjectId myID, @RequestBody JournalEntry newEntry) {
        System.out.println("Updating journal with ID: " + myID);  // Add this to confirm ID

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myID)).collect(Collectors.toList());
        System.out.println("Found matching entries: " + collect.size());  // Add this to check if entries match

        if (!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.findById(myID);
            if (journalEntry.isPresent()) {
                JournalEntry oldEntry = journalEntry.get();
                boolean updated = false;
                if (newEntry.getTitle() != null && !newEntry.getTitle().isEmpty() && !newEntry.getTitle().equals(oldEntry.getTitle())) {
                    oldEntry.setTitle(newEntry.getTitle());
                    updated = true;
                }
                if (newEntry.getContent() != null && !newEntry.getContent().isEmpty() && !newEntry.getContent().equals(oldEntry.getContent())) {
                    oldEntry.setContent(newEntry.getContent());
                    updated = true;
                }
                if (updated) {
                    // Log before analyzing emotion and sentiment
                    System.out.println("Before re-analyzing: " + oldEntry.getTitle() + " " + oldEntry.getContent());

                    // Sanitize the title and content before re-analyzing emotions and sentiment
                    String sanitizedTitle = emotionService.sanitizeContent(oldEntry.getTitle());
                    String sanitizedContent = emotionService.sanitizeContent(oldEntry.getContent());

                    // Re-analyze emotions and sentiment with sanitized content
                    EmotionAnalysisResponse emotionResponse = emotionService.analyzeEmotion(sanitizedTitle + " " + sanitizedContent);
                    oldEntry.setTopEmotion(emotionResponse.getTop_emotion());
                    oldEntry.setAllEmotions(emotionResponse.getAll_emotions());
                    oldEntry.setEmotion(emotionResponse.getTop_emotion().getLabel());

                    Sentiment sentiment = sentimentAnalysisService.getSentiment(sanitizedTitle + " " + sanitizedContent);
                    oldEntry.setSentiment(sentiment);

                    // Save the updated journal entry
                    journalEntryService.saveEntry(oldEntry);
                    System.out.println("Successfully updated journal entry");

                    return new ResponseEntity<>(oldEntry, HttpStatus.OK);
                }
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }



    @RestController
    @RequestMapping("/auth")  // Separate endpoint for authentication-related actions
    public class AuthController {

        // Logout functionality
        @PostMapping("/logout")
        public ResponseEntity<String> logout() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null) {
                // Invalidate the authentication
                SecurityContextHolder.clearContext();
                return ResponseEntity.ok("Logged out successfully");
            }
            return ResponseEntity.status(400).body("No user logged in");
        }
    }


}
