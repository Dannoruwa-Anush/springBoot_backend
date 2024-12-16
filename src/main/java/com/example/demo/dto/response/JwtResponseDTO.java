package com.example.demo.dto.response;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data //Generate Getter & Setters using Lombok 
public class JwtResponseDTO {
    // DTO specifies exactly what fields should be serialized.
    private String token;
    private Long userId;
    private String username;
    private String email;
    private Set<String> roles;
}
