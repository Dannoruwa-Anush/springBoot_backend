package com.example.demo.dto.request;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data // generate Getters and Setters using Lombok
public class UserStaffRegistrationDTO {
    
    private String username;
    private String email;
    private String address;
    private String telephoneNumber;
    private Set<String> expectingRoles;
}
