package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data //generate Getters and Setters using Lombok
public class UserPermissionRequestDTO {
    
    @NotBlank(message = "Permission name cannot be empty") //validation
    private String userPermissionName;
}
