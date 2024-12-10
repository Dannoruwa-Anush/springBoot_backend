package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.UserPermission;

@Service
public interface UserPermissionService {

    List<UserPermission> getAllUserPermissions();
    UserPermission getUserPermissionById(long id);
    UserPermission saveUserPermission(UserPermission userPermission);
    UserPermission updateUserPermission(long id, UserPermission userPermission);
    void deleteRole(long id);  
}
