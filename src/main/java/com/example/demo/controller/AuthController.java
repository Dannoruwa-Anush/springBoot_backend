package com.example.demo.controller;

import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.CustomerRegistrationDTO;
import com.example.demo.dto.request.UserLoginDTO;
import com.example.demo.service.UserService;

@RestController
public class AuthController {
    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    // ---
    @PostMapping("/auth/register")
    public ResponseEntity<?> registerUser(@RequestBody CustomerRegistrationDTO customerRegistrationDTO) {
        try {
            userService.addCustomer(customerRegistrationDTO);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "User account successfully created", "status", HttpStatus.CREATED.value()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage(), "status", HttpStatus.BAD_REQUEST.value()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage(), "status", HttpStatus.NOT_FOUND.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred", "status",
                            HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }
    // ---

    // ---
    @PostMapping("/auth/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDTO loginRequest) {
        try {
            return ResponseEntity.ok(userService.loginUser(loginRequest));
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage(), "status", HttpStatus.BAD_REQUEST.value()));
        } 
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of("error", "An unexpected error occurred", "status",
                    HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }  
    }
    // ---
}
