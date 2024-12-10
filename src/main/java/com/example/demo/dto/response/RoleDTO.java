package com.example.demo.dto.response;

import lombok.Data;

@Data //generate Getters and Setters using Lombok
public class RoleDTO {
    private Long id;
    private String roleName;
}
