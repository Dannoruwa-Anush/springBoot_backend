package com.example.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerRegistrationDTO {
    // DTO specifies exactly what fields should be serialized.
    private String username;
    private String email;
    private String password;
    private String address;
    private String telephoneNumber;
}
