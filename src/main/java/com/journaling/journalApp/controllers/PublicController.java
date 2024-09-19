package com.journaling.journalApp.controllers;

import com.journaling.journalApp.entity.User;
import com.journaling.journalApp.services.UserService;
import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoWriteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;


@Slf4j
@RestController
@RequestMapping("/public")

public class PublicController {

    @Autowired
    private UserService userService;


//    @PostMapping("/create-user")
//    public void createUser(@RequestBody User user){
//        userService.saveNewUser(user);
//    }


    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            userService.saveNewUser(user);
            return ResponseEntity.ok("User created successfully");
        } catch (org.springframework.dao.DuplicateKeyException e) {
            // Handle the DuplicateKeyException specifically
            return ResponseEntity.status(409).body("User with the same username already exists.");
        } catch (Exception e) {
            // Handle other exceptions
            return ResponseEntity.status(500).body("An error occurred while creating the user.");
        }
    }

    @GetMapping("/health-check")
    public String healthCheck(){
        return "Web Service is running Fine";
    }
}
