package com.example.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data // generate Getters and Setters using Lombok
public class UserRequestDTO {
    //DTO specifies exactly what fields should be serialized.
    private String username;
    private String email;
    private String address;
    private String telephoneNumber;
}
