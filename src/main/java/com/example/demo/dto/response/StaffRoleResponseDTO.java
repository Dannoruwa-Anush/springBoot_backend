package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data //generate Getters and Setters using Lombok
public class StaffRoleResponseDTO {
    // DTO specifies exactly what fields should be serialized.
    private Long id;
    private String roleName;
}
