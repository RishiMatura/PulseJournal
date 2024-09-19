package com.journaling.journalApp.controllers;

import com.journaling.journalApp.api.response.QuotesResponse;
import com.journaling.journalApp.api.response.WeatherResponse;
import com.journaling.journalApp.entity.JournalEntry;
import com.journaling.journalApp.entity.User;
import com.journaling.journalApp.repository.JournalEntryRepository;
import com.journaling.journalApp.repository.UserRepository;
import com.journaling.journalApp.serviceImpl.AudioServiceImpl;
import com.journaling.journalApp.services.QuotesService;
import com.journaling.journalApp.services.UserService;
import com.journaling.journalApp.services.WeatherService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")             // maps this whole class to this endpoint
//    All the controllers that are used in the project are stored in this directory

public class UserController {
    @Autowired      private UserService userService;
    @Autowired      private UserRepository userRepository;
    @Autowired      private WeatherService weatherService;
    @Autowired      private QuotesService quotesService;
    @Autowired      private JournalEntryRepository journalEntryRepository;
    @Autowired      private AudioServiceImpl audioServiceImpl;
//    Update an existing User

//   taking user obj that is already present in DB and then checking if its null, if not then setting the new userName and password in it and returning a Response Entity.
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User userInDB = userService.findByUserName(userName);
        if(userInDB != null){
            userInDB.setUserName(user.getUserName());
            userInDB.setPassword(user.getPassword());
            userService.saveNewUser(userInDB);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //       Delete User By Name
//    @DeleteMapping()
//    public ResponseEntity<?> deleteUserByID(){
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        userRepository.deleteByUserName(authentication.getName());
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }


    @DeleteMapping()
    public ResponseEntity<?> deleteUserByID() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        // Fetch all journal entries for the user
        List<JournalEntry> entries = journalEntryRepository.findByOwner(userName);

        // Delete associated audio files for each entry
        for (JournalEntry entry : entries) {
            String audioUrl = entry.getAudioUrl();
            if (audioUrl != null) {
                // Use the AudioServiceImpl to delete the audio file
                audioServiceImpl.deleteAudioFile(audioUrl);
            }
        }

        // Delete all journal entries for the user
        journalEntryRepository.deleteByOwner(userName);

        // Delete the user
        userRepository.deleteByUserName(userName);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }




    @GetMapping("/greetings")
    public ResponseEntity<?> greetings(@RequestParam(value = "city") String city, @RequestParam(value = "category") String category){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        WeatherResponse weatherResponse = weatherService.getWeather(city);
        String weatherReport = "";
        if(weatherResponse != null && weatherResponse.getCurrent() != null){
            weatherReport = " Weather feels like " + weatherResponse.getCurrent().getFeelslike();
        } else {
            weatherReport = " Weather information is not available.";
        }

        QuotesResponse quotesResponse = quotesService.getQuotes(category);
        String quoteGreeting = "";
        if (quotesResponse != null) {
            quoteGreeting = " Quote: '" + quotesResponse.getQuote() + "' - " + quotesResponse.getAuthor() + "\nCategory : " + quotesResponse.getCategory();
        } else {
            quoteGreeting = " Quote information is not available.";
        }

        return new ResponseEntity<>("Hi " + authentication.getName() + "\n" + weatherReport + "\n" + quoteGreeting, HttpStatus.OK);
    }

//    @GetMapping()
//    public ResponseEntity<?> greetings(){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        WeatherResponse weatherResponse = weatherService.getWeather("Mumbai");
//        String weatherReport = "";
//        if(weatherResponse != null){
//            weatherReport = " Weather feels like " + weatherResponse.getCurrent().getFeelslike();
//        }
//        QuotesResponse quotesResponse = quotesService.getQuotes("happiness");
//        String quoteGreeting = "";
//        if (quotesResponse != null) {
//            quoteGreeting = " Quote: '" + quotesResponse.getQuote() + "' - " + quotesResponse.getAuthor();
//        }
//
//
//        return new ResponseEntity<>("Hi " + authentication.getName() + weatherReport + "\n" + quoteGreeting, HttpStatus.OK);
//    }
    
}
