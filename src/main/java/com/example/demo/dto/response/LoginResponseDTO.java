package com.example.demo.dto.response;

import java.util.Set;

import com.example.demo.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data //Generate Getter & Setters using Lombok 
@AllArgsConstructor
public class LoginResponseDTO {
    // DTO specifies exactly what fields should be serialized.
    private String token;
    private String username;
    private Set<Role> roles;
}
