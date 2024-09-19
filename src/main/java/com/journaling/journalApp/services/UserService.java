package com.journaling.journalApp.services;

import com.journaling.journalApp.entity.User;
import com.journaling.journalApp.repository.UserRepository;
import com.mongodb.DuplicateKeyException;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class UserService {

    @Autowired
    UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    public void saveNewUser(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword())); // For encoding password

            User existingUser = userRepository.findByUserName(user.getUserName());

            if (existingUser != null && (user.getRoles() == null || user.getRoles().isEmpty())) {
                user.setRoles(existingUser.getRoles()); // Retain existing roles if not provided
            } else if (user.getRoles() == null || user.getRoles().isEmpty()) {
                user.setRoles(Arrays.asList("USER")); // Set default role if roles are not provided
            }

            userRepository.save(user); // This is where the DuplicateKeyException can occur

        } catch (org.springframework.dao.DuplicateKeyException e) {
            // Log the exception and rethrow it
            log.error("Duplicate key error for user: {}", user.getUserName(), e);
            throw e; // Rethrow the exception so it can be caught in the controller
        } catch (Exception e) {
            log.error("Error occurred for {}: ", user.getUserName(), e);
            throw e; // Rethrow other exceptions
        }
    }



    public void saveUser(User user){             //For Saving the encoded password...
        userRepository.save(user);
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }


    public void deleteById(ObjectId id){
        userRepository.deleteById(id);
    }

    public User findByUserName(String userName){
        return userRepository.findByUserName(userName);
    }

    public void saveAdmin(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));       //For Saving the encoded password...
        user.setRoles(Arrays.asList("USER", "ADMIN"));
        userRepository.save(user);
    }
}

//controller ---> service ---> repository
//controller will call service, which will call repository