package com.example.demo.dto.response.getById;

import java.util.List;

import com.example.demo.dto.response.UserRoleResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data //generate Getters and Setters using Lombok
public class StaffGetByIdResponseDTO {
    // DTO specifies exactly what fields should be serialized.
    private Long id;
    private String username;
    private String email;
    private String address;
    private String telephoneNumber;

    private List<UserRoleResponseDTO> roles;
}
