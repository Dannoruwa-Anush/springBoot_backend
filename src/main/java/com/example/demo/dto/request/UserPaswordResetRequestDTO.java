package com.example.demo.dto.request;

import lombok.Data;

@Data //generate Getters and Setters using Lombok
public class UserPaswordResetRequestDTO {
    private Long id;
    private String newPassword;
}
