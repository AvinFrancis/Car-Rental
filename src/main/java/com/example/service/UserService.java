package com.example.service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.repository.UserRepository;
import com.example.model.User;

@Service
public class UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getallUsers(){
        logger.info("Fetching all users");
        List<User> users = userRepository.findAll();
        logger.debug("Retrieved {} users", users.size());
        return users;
    }

    public User getUserById(Long id){
        logger.info("Fetching user with ID: {}", id);
        return userRepository.findById(id)
            .orElseThrow(() -> {
                logger.warn("User not found with ID: {}", id);
                return new RuntimeException("User Not Found with ID: " + id);
            });
    }
    
    public User createUser(User user) {
        logger.info("Creating new user with email: {}", user.getEmail());
        Optional<User> existing = userRepository.findByEmail(user.getEmail());
        if (existing.isPresent()) {
            logger.warn("User already exists with email: {}", user.getEmail());
            throw new RuntimeException("User already exists with email: " + user.getEmail());
        }

        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);
            logger.debug("Successfully created user with ID: {}", savedUser.getId());
            return savedUser;
        } catch (Exception e) {
            logger.error("Failed to create user with email: {}: {}", user.getEmail(), e.getMessage(), e);
            throw e;
        }
    }

    public User updateUser(Long id, User updatedUser) {    
        logger.info("Updating user with ID: {} with details: {}", id, updatedUser);
        return userRepository.findById(id).map(user -> {
            logger.debug("Found user with ID: {}, updating details", id);
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            user.setRole(updatedUser.getRole());
            User savedUser = userRepository.save(user);
            logger.debug("Successfully updated user with ID: {}", id);
            return savedUser;
        }).orElseThrow(() -> {
            logger.warn("User not found for update with ID: {}", id);
            return new RuntimeException("User not found");
        });
    }

    public void deleteUser(Long id) {
        logger.info("Deleting user with ID: {}", id);
        try {
            userRepository.deleteById(id);
            logger.debug("Successfully deleted user with ID: {}", id);
        } catch (Exception e) {
            logger.error("Failed to delete user with ID: {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
}