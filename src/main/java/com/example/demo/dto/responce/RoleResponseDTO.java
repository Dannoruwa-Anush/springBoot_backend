package com.example.demo.dto.responce;

import java.util.Set;

import com.example.demo.entity.UserPermission;

import lombok.Data;

@Data //generate Getters and Setters using Lombok
public class RoleResponseDTO {
    private Long roleId;
    private String roleName;
    private Set<UserPermission> userPermissions;   
}
