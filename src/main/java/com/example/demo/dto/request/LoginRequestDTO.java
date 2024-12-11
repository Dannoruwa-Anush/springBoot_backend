package com.example.demo.dto.request;

import lombok.Data;

@Data //Generate Getter & Setters using Lombok 
public class LoginRequestDTO {

    private String username;
    private String password;
}
