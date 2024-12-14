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

import com.example.demo.common.customHttpResponse.CustomErrorResponse;
import com.example.demo.dto.request.RoleRequestDTO;
import com.example.demo.dto.response.RoleResponseDTO;
import com.example.demo.service.RoleService;

@RestController
@RequestMapping("role")
public class RoleController {

    @Autowired
    private RoleService roleService;

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
    public ResponseEntity<List<RoleResponseDTO>> getAllRoles() {
        List<RoleResponseDTO> roles = roleService.getAllRoles();

        if(roles.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> getRoleById(@PathVariable long id) {
        try {
            RoleResponseDTO role = roleService.getRoleById(id);
            return ResponseEntity.status(HttpStatus.OK).body(role);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<Object> createRole(@RequestBody RoleRequestDTO roleRequestDTO) {
        try {
            RoleResponseDTO createdRole = roleService.saveRole(roleRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomErrorResponse(e.getMessage()));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateRole(@PathVariable long id, @RequestBody RoleRequestDTO roleRequestDTO) {
        try {
            RoleResponseDTO updatedRole = roleService.updateRole(id, roleRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body(updatedRole);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomErrorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role is not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteRole(@PathVariable long id) {
        try {
            roleService.deleteRole(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomErrorResponse(e.getMessage()));
        }catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role is not found");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
}
