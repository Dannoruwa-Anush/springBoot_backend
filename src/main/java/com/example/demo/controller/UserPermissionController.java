package com.example.demo.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.customHttpResponse.CustomErrorResponse;
import com.example.demo.dto.response.UserPermissionDTO;
import com.example.demo.entity.UserPermission;
import com.example.demo.service.UserPermissionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("userpermissions")
public class UserPermissionController {

    @Autowired
    private UserPermissionService userPermissionService;

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
    
    private UserPermissionDTO toDTO(UserPermission userPermission){
        UserPermissionDTO dto = new UserPermissionDTO();
        dto.setId(userPermission.getId());
        dto.setUserPermissionName(userPermission.getUserPermissionName());
        return dto;
    }

    private UserPermission toEntity(UserPermissionDTO dto){
        UserPermission userPermission = new UserPermission();
        userPermission.setId(dto.getId());
        userPermission.setUserPermissionName(dto.getUserPermissionName());
        return userPermission;
    }

    @GetMapping
    public ResponseEntity<List<UserPermissionDTO>> getAllUserPermission() {
        List<UserPermission> userPermissions = userPermissionService.getAllUserPermissions();

        if(userPermissions.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        List<UserPermissionDTO> userPermissionDTOs = userPermissions.stream().map(this::toDTO).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(userPermissionDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserPermissionDTO> getUserPermissionById(@PathVariable long id) {
        try {
            UserPermission userPermission = userPermissionService.getUserPermissionById(id);
            return ResponseEntity.status(HttpStatus.OK).body(toDTO(userPermission));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<Object> createUserPermission(@RequestBody UserPermissionDTO userPermissionDTO) {
        try {
            UserPermission createdUserPermission = userPermissionService.saveUserPermission(toEntity(userPermissionDTO));
            return ResponseEntity.status(HttpStatus.OK).body(toDTO(createdUserPermission));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomErrorResponse(e.getMessage()));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUserPermission(@PathVariable Long id, @RequestBody UserPermissionDTO userPermissionDTO) {
        try {
            UserPermission updatedUserPermission = userPermissionService.updateUserPermission(id, toEntity(userPermissionDTO));
            return ResponseEntity.status(HttpStatus.OK).body(toDTO(updatedUserPermission));
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomErrorResponse(e.getMessage()));
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Permission is not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUserPermission(@PathVariable Long id){
        try {
            userPermissionService.deleteRole(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomErrorResponse(e.getMessage()));
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Permission is not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
}
