package com.example.demo.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/users") //serve entire controller under users url 
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
    }
    // ---

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable long id) {
        try {
            // Try to find user by id
            User user = userService.getUserById(id);
            return ResponseEntity.status(HttpStatus.OK).body(user);

        } catch (NoSuchElementException e) {
            // If there is no user by id, throw NoSuchElementException from
            // UserServiceIml.java
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    // ---

    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        try {
            // Try to find user by id
            User userControll = userService.saveUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(userControll);

        } catch (NoSuchElementException e) {
            // If there is no user by id, throw NoSuchElementException from
            // UserServiceIml.java
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    // ---

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable long id, @RequestBody User user) {
        try {
            User updateUser = userService.updateUser(id, user);
            return ResponseEntity.status(HttpStatus.OK).body(updateUser);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    // ---

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }
    // ---
}
