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
    private UserPermissionResponseDTO toResponseDTO(UserPermission userPermission) {
        UserPermissionResponseDTO dto = new UserPermissionResponseDTO();
        dto.setId(userPermission.getId());
        dto.setUserPermissionName(userPermission.getUserPermissionName());
        return dto;
    }

    private UserPermission toEntity(UserPermissionRequestDTO dto) {
        UserPermission userPermission = new UserPermission();
        userPermission.setUserPermissionName(dto.getUserPermissionName());
        return userPermission;
    }
    // ********

    @Override
    public List<UserPermissionResponseDTO> getAllUserPermissions() {
        List<UserPermission> userPermissions = userPermissionRepository.findAll();
        List<UserPermissionResponseDTO> userPermissionDTOs = userPermissions.stream().map(this::toResponseDTO)
                .collect(Collectors.toList());
        return userPermissionDTOs;
    }

    @Override
    public UserPermissionResponseDTO getUserPermissionById(long id) {
        UserPermission userPermission = userPermissionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Permission is not found " + id));

        return toResponseDTO(userPermission);
    }

    @Override
    public UserPermissionResponseDTO saveUserPermission(UserPermissionRequestDTO userPermissionRequestDTO) {
        Optional<UserPermission> existingUserPermission = userPermissionRepository
                .findByUserPermissionName(userPermissionRequestDTO.getUserPermissionName());
        if (existingUserPermission.isPresent()) {
            throw new IllegalArgumentException(
                    "Permission with name" + userPermissionRequestDTO.getUserPermissionName() + " already exists");
        }

        // convert dto to entity before save
        UserPermission saveToUserPermission = toEntity(userPermissionRequestDTO);

        return toResponseDTO(userPermissionRepository.save(saveToUserPermission));
    }

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

        return toResponseDTO(userPermissionRepository.save(existingUserPermission));
    }

    @Override
    public void deleteRole(long id) {
        if (!userPermissionRepository.existsById(id)) {
            throw new IllegalArgumentException("Permission is not found with id: " + id);
        }
        userPermissionRepository.deleteById(id);
        logger.info("Permission with id {} was deleted.", id);
    }
}
