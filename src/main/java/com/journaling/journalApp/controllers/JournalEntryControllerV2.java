package com.journaling.journalApp.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.journaling.journalApp.entity.JournalEntry;
import com.journaling.journalApp.entity.User;
import com.journaling.journalApp.serviceImpl.AudioServiceImpl;
import com.journaling.journalApp.services.AudioService;
import com.journaling.journalApp.services.JournalEntryService;
import com.journaling.journalApp.services.UserService;
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

@RestController
@RequestMapping("/journal")             // maps this whole class to this endpoint
//    All the controllers that are used in the project are stored in this directory

public class JournalEntryControllerV2 {
    @Autowired          private JournalEntryService journalEntryService;
    @Autowired          private UserService userService;
    @Autowired          private RestTemplate restTemplate;
    @Autowired          private AudioService audioService;


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
public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userName = authentication.getName();

    try {
        myEntry.setOwner(userName);


        // Convert the title + content to audio using AudioService

        String fileName = audioService.generateFileName(myEntry); // Assuming myEntry is your JournalEntry object
        String audioUrl = audioService.convertTextToSpeech(myEntry.getTitle() + " " + myEntry.getContent(), fileName);


//        String audioUrl = audioService.convertTextToSpeech(myEntry.getTitle() + " " + myEntry.getContent());

        // Set the generated audio URL to the journal entry
        myEntry.setAudioUrl(audioUrl);

        journalEntryService.saveEntry(myEntry, userName);
        return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
    } catch (Exception e) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
            String audioUrl = entry.getAudioUrl();
            if (audioUrl != null && !audioUrl.isEmpty()) {
                audioService.deleteAudioFile(audioUrl);  // Method to delete audio file
            }
            // Delete the journal entry from the database
            journalEntryService.deleteById(myID, userName);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // User is not allowed to delete this entry
        }
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
}


    //  5)     Update Entry by ID
    @PutMapping("id/{myID}")
    public ResponseEntity<?> updateJournalByID(@PathVariable ObjectId myID, @RequestBody JournalEntry newEntry) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myID)).collect(Collectors.toList());

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
                    audioService.replaceAudioFile(oldEntry, oldEntry.getTitle() + " " + oldEntry.getContent());
                    journalEntryService.saveEntry(oldEntry);
                }
                return new ResponseEntity<>(oldEntry, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }



}
