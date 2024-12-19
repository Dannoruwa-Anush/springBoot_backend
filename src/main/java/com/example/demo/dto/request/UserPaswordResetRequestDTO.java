package com.example.demo.dto.request;

import lombok.Data;

@Data //generate Getters and Setters using Lombok
public class UserPaswordResetRequestDTO {
    // DTO specifies exactly what fields should be serialized.
    private String newPassword;
}
