//package com.journaling.journalApp.controllers;
//
//import com.journaling.journalApp.entity.User;
//import com.journaling.journalApp.services.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/auth")
//public class AuthController {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody User loginRequest) {
//        // Authenticate the user
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword())
//        );
//
//        // Set authentication in SecurityContext
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        return ResponseEntity.ok("Login successful");
//    }
//
//    @PostMapping("/logout")
//    public ResponseEntity<String> logout() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication != null) {
//            // Invalidate the authentication
//            SecurityContextHolder.clearContext();
//            return ResponseEntity.ok("Logged out successfully");
//        }
//        return ResponseEntity.status(400).body("No user logged in");
//    }
//}
//
