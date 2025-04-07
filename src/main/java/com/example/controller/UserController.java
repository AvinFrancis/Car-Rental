package com.example.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.model.User;
import com.example.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User registerUser(@RequestBody User user){
        logger.info("Registering new user: {}", user);
        try {
            User createdUser = userService.createUser(user);
            logger.debug("Successfully registered user with ID: {}", createdUser.getId());
            return createdUser;
        } catch (Exception e) {
            logger.error("Failed to register user: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping
    public List<User> getAllUsers(){
        logger.info("Fetching all users");
        List<User> users = userService.getallUsers();
        logger.debug("Retrieved {} users", users.size());
        return users;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        logger.info("Fetching user with ID: {}", id);
        User user = userService.getUserById(id);
        if (user != null) {
            logger.debug("Successfully retrieved user: {}", user);
        } else {
            logger.warn("User not found with ID: {}", id);
        }
        return user;
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        logger.info("Updating user with ID: {} with details: {}", id, user);
        User updatedUser = userService.updateUser(id, user);
        logger.debug("Successfully updated user with ID: {}", id);
        return updatedUser;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        logger.info("Deleting user with ID: {}", id);
        userService.deleteUser(id);
        logger.debug("Successfully deleted user with ID: {}", id);
    }
}