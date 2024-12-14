package com.example.demo.dto.response;

import java.util.Set;

import lombok.Data;

@Data //generate Getters and Setters using Lombok
public class RoleResponseDTO {
    private Long roleId;
    private String roleName;
    private Set<String> userPermissionNames;   
}
