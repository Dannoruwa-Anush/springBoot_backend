package com.example.demo.controller;

import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.customException.FirstLoginCustomException;
import com.example.demo.dto.request.CustomerRegistrationDTO;
import com.example.demo.dto.request.UserFindRequestDTO;
import com.example.demo.dto.request.UserLoginRequestDTO;
import com.example.demo.dto.request.UserPaswordResetRequestDTO;
import com.example.demo.dto.response.UserResponseDTO;
import com.example.demo.service.UserService;

@RestController
/*
 * It allows you to specify which external origins (i.e., domains or URLs) are
 * permitted to make requests to your API. This is useful when your frontend
 * application (running on a different server or port) needs to interact with
 * your backend
 */
@CrossOrigin(origins = "*")
public class AuthController {
    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/auth/register")
    public ResponseEntity<Object> registerUser(@RequestBody CustomerRegistrationDTO customerRegistrationDTO) {
        try {
            UserResponseDTO userResponseDTO = userService.addCustomer(customerRegistrationDTO);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "User account successfully created", "status", HttpStatus.CREATED.value(),"response", userResponseDTO));
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

    @PostMapping("/auth/login")
    public ResponseEntity<Object> loginUser(@RequestBody UserLoginRequestDTO loginRequest) {
        try {
            return ResponseEntity.ok(userService.loginUser(loginRequest));
        } catch (FirstLoginCustomException e) {
            /*
             * Browser automatically follows the 302-Found response to /redirection location
             * due to the Location header.
             */

            // Redirect to password-reset page for first-time login
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", "/auth/password-reset-page") //For redirection, it should naviate to GET request
                    .body(Map.of("message", e.getMessage(), "redirect", "/auth/password-reset-page"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage(), "status", HttpStatus.BAD_REQUEST.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred", "status",
                            HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }
    // ---

    @PutMapping("/auth/password-reset")
    public ResponseEntity<Object> resetPassword(@RequestBody UserPaswordResetRequestDTO userPaswordResetRequestDTO) {
        try {
            userService.isPasswordReset(userPaswordResetRequestDTO);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "Password reset successful", "status", HttpStatus.OK.value()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Soory, password reset failed");
        }
    }
    // ---

    // ---
    @GetMapping("/auth/password-reset-page")
    public ResponseEntity<Object> showPasswordResetPage() {
        return ResponseEntity.ok("Please enter your new password.");
    }
    // ---

    @PostMapping("/auth/userRecovery")
    public ResponseEntity<UserResponseDTO> getUserByUsername(@RequestBody UserFindRequestDTO userFindRequestDTO) {
        try {
            UserResponseDTO user = userService.getUserByUsername(userFindRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
