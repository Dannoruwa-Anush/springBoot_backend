package com.example.demo.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.request.UserPermissionRequestDTO;
import com.example.demo.dto.response.UserPermissionResponseDTO;
import com.example.demo.entity.UserPermission;
import com.example.demo.repository.UserPermissionRepository;

@Service
public class UserPermissionServiceImpl implements UserPermissionService {

    @Autowired
    private UserPermissionRepository userPermissionRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserPermissionServiceImpl.class);

    // ********
    private UserPermissionResponseDTO toUserPermissionResponseDTO(UserPermission userPermission) {
        UserPermissionResponseDTO dto = new UserPermissionResponseDTO();
        dto.setId(userPermission.getId());
        dto.setUserPermissionName(userPermission.getUserPermissionName());
        return dto;
    }
    //---
    // ********

    @Override
    public List<UserPermissionResponseDTO> getAllUserPermissions() {
        List<UserPermission> userPermissions = userPermissionRepository.findAll();
        List<UserPermissionResponseDTO> userPermissionDTOs = userPermissions.stream().map(this::toUserPermissionResponseDTO)
                .collect(Collectors.toList());
        return userPermissionDTOs;
    }
    //---

    @Override
    public UserPermissionResponseDTO getUserPermissionById(long id) {
        UserPermission userPermission = userPermissionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Permission is not found " + id));

        return toUserPermissionResponseDTO(userPermission);
    }
    //---

    @Override
    public UserPermissionResponseDTO saveUserPermission(UserPermissionRequestDTO userPermissionRequestDTO) {
        Optional<UserPermission> existingUserPermission = userPermissionRepository
                .findByUserPermissionName(userPermissionRequestDTO.getUserPermissionName());
        if (existingUserPermission.isPresent()) {
            throw new IllegalArgumentException(
                    "Permission with name" + userPermissionRequestDTO.getUserPermissionName() + " already exists");
        }

        //create new userPermission
        UserPermission saveToUserPermission = new UserPermission();
        saveToUserPermission.setUserPermissionName(userPermissionRequestDTO.getUserPermissionName());

        return toUserPermissionResponseDTO(userPermissionRepository.save(saveToUserPermission));
    }
    //---

    @Override
    public UserPermissionResponseDTO updateUserPermission(long id, UserPermissionRequestDTO userPermissionRequestDTO) {
        // Get existing UserPermission
        UserPermission existingUserPermission = userPermissionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Permission is not found " + id));

        if ((existingUserPermission.getUserPermissionName().equals(userPermissionRequestDTO.getUserPermissionName()))  &&
        userPermissionRepository.findByUserPermissionName(userPermissionRequestDTO.getUserPermissionName()).isPresent()) {
            throw new IllegalArgumentException(
                    "Permission with name" + userPermissionRequestDTO.getUserPermissionName() + " already exists");
        }

        // update
        existingUserPermission.setUserPermissionName(userPermissionRequestDTO.getUserPermissionName());

        return toUserPermissionResponseDTO(userPermissionRepository.save(existingUserPermission));
    }
    //---

    @Override
    public void deleteRole(long id) {
        if (!userPermissionRepository.existsById(id)) {
            throw new IllegalArgumentException("Permission is not found with id: " + id);
        }

        // Check if any roles are associated with the userpermission
        UserPermission userPermission = userPermissionRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Permission is not found " + id));

        if(userPermission.getRoles() != null && !userPermission.getRoles().isEmpty()){
            throw new IllegalStateException("Cannot delete Permission because it is assigned to one or more roles.");
        }

        userPermissionRepository.deleteById(id);
        logger.info("Permission with id {} was deleted.", id);
    }
    //---
}
