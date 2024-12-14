package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.request.UserPermissionRequestDTO;
import com.example.demo.dto.response.UserPermissionResponseDTO;
import com.example.demo.entity.UserPermission;

@Service
public interface UserPermissionService {

    List<UserPermissionResponseDTO> getAllUserPermissions();
    UserPermissionResponseDTO getUserPermissionById(long id);
    UserPermissionResponseDTO saveUserPermission(UserPermissionRequestDTO userPermissionRequestDTO);
    UserPermissionResponseDTO updateUserPermission(long id, UserPermissionRequestDTO uPermissionRequestDTO);
    void deleteRole(long id);  

    //Entity - DTO convertors
    UserPermissionResponseDTO toUserPermissionResponseDTO(UserPermission userPermission);
    UserPermission toUserPermissionEntity(UserPermissionRequestDTO dto);
}
