package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.request.CustomerRegistrationDTO;
import com.example.demo.dto.request.UserFindRequestDTO;
import com.example.demo.dto.request.UserLoginRequestDTO;
import com.example.demo.dto.request.UserPaswordResetRequestDTO;
import com.example.demo.dto.request.UserRequestDTO;
import com.example.demo.dto.request.UserStaffRegistrationRequestDTO;
import com.example.demo.dto.response.JwtResponseDTO;
import com.example.demo.dto.response.UserResponseDTO;

@Service
public interface UserService {
    
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO getUserById(long id);
    UserResponseDTO updateUserProfile(long id, UserRequestDTO userRequestDTO);
    void deleteUser(long id);  
    UserResponseDTO addStaff(UserStaffRegistrationRequestDTO userStaffRegistrationRequestDTO);


    //for AuthController
    boolean isPasswordReset(UserPaswordResetRequestDTO userPaswordResetRequestDTO);
    UserResponseDTO addCustomer(CustomerRegistrationDTO customerRegistrationDTO);
    JwtResponseDTO loginUser(UserLoginRequestDTO loginRequest);
    UserResponseDTO getUserByUsername(UserFindRequestDTO userFindRequestDTO);
}
