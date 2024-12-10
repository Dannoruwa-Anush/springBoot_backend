package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.request.UserPaswordResetRequestDTO;
import com.example.demo.dto.request.UserStaffRegistrationDTO;
import com.example.demo.dto.response.UserDTO;
import com.example.demo.entity.User;

@Service
public interface UserService {
    
    List<User> getAllUsers();
    User getUserById(long id);
    User saveUser(User user);
    User updateUserProfile(long id, UserDTO userDTO);
    void deleteUser(long id);  

    boolean isPasswordReset(UserPaswordResetRequestDTO userPaswordResetRequestDTO);
    User addStaff(UserStaffRegistrationDTO userStaffRegistrationDTO);
}
