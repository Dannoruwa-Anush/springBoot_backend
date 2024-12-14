package com.example.demo.dto.response;

import java.util.Set;

import lombok.Data;

@Data //generate Getters and Setters using Lombok
public class RoleResponseDTO {
    //DTO specifies exactly what fields should be serialized.
    private Long id;
    private String roleName;
    private Set<String> userPermissionNames;   
}
