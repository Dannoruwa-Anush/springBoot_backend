package com.example.demo.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.UserPermission;
import com.example.demo.repository.UserPermissionRepository;

@Service
public class UserPermissionServiceImpl implements UserPermissionService{

    @Autowired
    private UserPermissionRepository userPermissionRepository;

    private static final Logger logger =LoggerFactory.getLogger(UserPermissionServiceImpl.class);

    @Override
    public List<UserPermission> getAllUserPermissions() {
        return userPermissionRepository.findAll();
    }

    @Override
    public UserPermission getUserPermissionById(long id) {
       return userPermissionRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Permission is not found " +id));
    }

    @Override
    public UserPermission saveUserPermission(UserPermission userPermission) {
        Optional<UserPermission> existingUserPermission = userPermissionRepository.findByUserPermissionName(userPermission.getUserPermissionName());
        if(existingUserPermission.isPresent()){
            throw new IllegalArgumentException("Permission with name" + userPermission.getUserPermissionName() + " already exists");
        }
        return userPermissionRepository.save(userPermission);
    }

    @Override
    public UserPermission updateUserPermission(long id, UserPermission userPermission) {
        UserPermission existingUserPermission = getUserPermissionById(id);

        if((existingUserPermission.getUserPermissionName().equals(userPermission.getUserPermissionName()))){
            throw new IllegalArgumentException("Permission with name" + userPermission.getUserPermissionName() + " already exists");
        }

        existingUserPermission.setUserPermissionName(userPermission.getUserPermissionName());
        return userPermissionRepository.save(existingUserPermission);
    }

    @Override
    public void deleteRole(long id) {
        Optional<UserPermission> existingUserPermission = userPermissionRepository.findById(id);
        
        if(!existingUserPermission.isPresent()){
            throw new IllegalArgumentException("Permission is not found with id: " + id);
        }
        userPermissionRepository.deleteById(id);
        logger.info("Permission with id {} was deleted.", id);
    }
}
