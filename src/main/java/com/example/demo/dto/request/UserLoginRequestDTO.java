package com.example.demo.dto.request;

import lombok.Data;

@Data //Generate Getter & Setters using Lombok 
public class UserLoginRequestDTO {
    // DTO specifies exactly what fields should be serialized.
    private String username;
    private String password;
}
