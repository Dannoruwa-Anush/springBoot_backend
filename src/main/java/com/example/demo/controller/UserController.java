package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.common.customHttpResponse.CustomErrorResponse;
import com.example.demo.dto.request.UserRequestDTO;
import com.example.demo.dto.request.UserStaffRegistrationRequestDTO;
import com.example.demo.dto.request.StaffUserRequestDTO;
import com.example.demo.dto.response.UserResponseDTO;
import com.example.demo.dto.response.getById.StaffGetByIdResponseDTO;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/user") // set table name
/*
 * It allows you to specify which external origins (i.e., domains or URLs) are
 * permitted to make requests to your API. This is useful when your frontend
 * application (running on a different server or port) needs to interact with
 * your backend
 */
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    /*
     * ResponseEntity is a powerful class in Spring Boot for managing HTTP
     * responses.
     * 
     * It allows you to:
     * 
     * Return custom status codes.
     * Add headers.
     * Set the body of the response.
     * 
     * .build() - You typically use .build() when you want to send an HTTP status
     * without any associated content in the response body.
     */

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();

        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(users);
    }
    // ---

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable long id) {
        try {
            UserResponseDTO user = userService.getUserById(id);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    // ---

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUserProfile(@PathVariable long id, @RequestBody UserRequestDTO userRequestDTO) {
        try {
            UserResponseDTO updatedUser = userService.updateUserProfile(id, userRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomErrorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User is not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
    // ---

    // add staff - this is alloacted for Admin role
    @PostMapping("/add-staff")
    public ResponseEntity<Object> addStaff(@RequestBody UserStaffRegistrationRequestDTO userStaffRegistrationRequestDTO) {
        try {
            UserResponseDTO addedStaff = userService.addStaff(userStaffRegistrationRequestDTO);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Staff user successfully created", "status", HttpStatus.CREATED.value(), "response", addedStaff));
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

    // get all staff - this is alloacted for Admin role
    @GetMapping("/staff")
    public ResponseEntity<List<UserResponseDTO>> getAllStaffMembers() {
        List<UserResponseDTO> users = userService.getAllStaffMembers();

        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(users);
    }
    //----

    // get staff by id - this is alloacted for Admin role
    @GetMapping("/staff/{id}")
    public ResponseEntity<StaffGetByIdResponseDTO> getStaffMemberById(@PathVariable long id) {
        try {
            StaffGetByIdResponseDTO user = userService.getStaffMemberById(id);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    //----

    // get update staff - this is alloacted for Admin role
    @PutMapping("/staff/{id}")
    public ResponseEntity<Object> updateStaffMember(@PathVariable long id, @RequestBody StaffUserRequestDTO userUpdateRequestDTO) {
        try {
            UserResponseDTO updatedUser = userService.updateStaffMember(id, userUpdateRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomErrorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User is not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
    //---

    // get delete staff - this is alloacted for Admin role
    @DeleteMapping("/staff/{id}")
    public ResponseEntity<Object> deleteStaffMember(@PathVariable long id) {
        try {
            userService.deleteStaffMember(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomErrorResponse(e.getMessage()));
        }catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Staff member is not found");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
    //----
}
