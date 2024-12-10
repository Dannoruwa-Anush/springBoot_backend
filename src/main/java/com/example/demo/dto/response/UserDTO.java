package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data // generate Getters and Setters using Lombok
public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private String address;
    private String telephoneNumber;
}
