package com.example.demo.dto.request;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data //generate Getters and Setters using Lombok
public class RoleRequestDTO {
    private Long roleId;
    private String roleName;
    private Set<Long> permissionIds;
}
