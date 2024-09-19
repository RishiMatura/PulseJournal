package com.journaling.journalApp.controllers;

import com.journaling.journalApp.entity.User;
import com.journaling.journalApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    UserService userService;

    //    Get all the Users
    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers(){
        List<User> all =  userService.getAll();
        if(all != null && !all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create-admin-user")
    public ResponseEntity<?> createAdminUser(@RequestBody User user){
        try {
            userService.saveNewUser(user);
            return ResponseEntity.ok("Admin User created successfully");
        } catch (org.springframework.dao.DuplicateKeyException e) {
            // Handle the DuplicateKeyException specifically
            return ResponseEntity.status(409).body("User with the same username already exists.");
        } catch (Exception e) {
            // Handle other exceptions
            return ResponseEntity.status(500).body("An error occurred while creating the user.");
        }
    }
}
