package com.example.demo.service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.request.RoleRequestDTO;
import com.example.demo.dto.response.RoleResponseDTO;
import com.example.demo.dto.response.UserPermissionResponseDTO;
import com.example.demo.entity.Role;
import com.example.demo.entity.UserPermission;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserPermissionRepository;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserPermissionRepository userPermissionRepository;

    @Autowired
    private UserPermissionService userPermissionService; // to use DTO Convertor

    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    // ****
    @Override
    public RoleResponseDTO toRoleResponseDTO(Role role) {
        RoleResponseDTO responseDto = new RoleResponseDTO();
        responseDto.setRoleId(role.getId());
        responseDto.setRoleName(role.getRoleName());

        // Convert UserPermissions to DTOs
        Set<UserPermissionResponseDTO> userPermissionDTOs = role.getUserPermissions().stream()
                .map(userPermissionService::toUserPermissionResponseDTO)
                .collect(Collectors.toSet());

        responseDto.setUserPermissions(userPermissionDTOs);

        return responseDto;
    }
    //---

    @Override
    public Role toRoleEntity(RoleRequestDTO dto) {
        Role role = new Role();
        role.setRoleName(dto.getRoleName());

        // Fetch and set permissions
        List<UserPermission> requestPermissionList = userPermissionRepository.findAllById(dto.getPermissionIds());

        // Use a temporary set to safely handle permissions
        Set<UserPermission> requestPermissionSet = new HashSet<>(requestPermissionList);
        role.setUserPermissions(requestPermissionSet);

        return role;
    }
    // ****

    @Override
    public List<RoleResponseDTO> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        List<RoleResponseDTO> roleDTOs = roles.stream().map(this::toRoleResponseDTO)
                .collect(Collectors.toList());
        return roleDTOs;
    }
    //---

    @Override
    public RoleResponseDTO getRoleById(long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Role is not found " + id));
        return toRoleResponseDTO(role);
    }
    //---

    @Override
    public RoleResponseDTO saveRole(RoleRequestDTO roleRequestDTO) {
        Optional<Role> existingRole = roleRepository.findByRoleName(roleRequestDTO.getRoleName());
        if (existingRole.isPresent()) {
            throw new IllegalArgumentException("Role with name '" + roleRequestDTO.getRoleName() + "' already exists.");
        }

        // convert dto to entity
        Role saveToRole = toRoleEntity(roleRequestDTO);
        return toRoleResponseDTO(roleRepository.save(saveToRole));
    }
    //---

    @Override
    public RoleResponseDTO updateRole(long id, RoleRequestDTO roleRequestDTO) {
        // Retrieve the existing role
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Role is not found with ID: " + id));

        // Check if the role name is being updated to one that already exists
        if (!existingRole.getRoleName().equals(roleRequestDTO.getRoleName()) &&
                roleRepository.findByRoleName(roleRequestDTO.getRoleName()).isPresent()) {
            throw new IllegalArgumentException("Role with name '" + roleRequestDTO.getRoleName() + "' already exists.");
        }

        // Update the role name
        existingRole.setRoleName(roleRequestDTO.getRoleName());

        // Fetch the requested permissions
        Set<UserPermission> requestedPermissions = new HashSet<>(
                userPermissionRepository.findAllById(roleRequestDTO.getPermissionIds()));

        // Update permissions:
        // 1. Add new permissions from the request
        requestedPermissions.forEach(permission -> {
            if (!existingRole.getUserPermissions().contains(permission)) {
                existingRole.getUserPermissions().add(permission);
            }
        });

        // 2. Remove permissions not in the request
        existingRole.getUserPermissions().removeIf(permission -> !requestedPermissions.contains(permission));

        // Save the updated role
        Role updatedRole = roleRepository.save(existingRole);

        // Convert to DTO and return
        return toRoleResponseDTO(updatedRole);
    }
    //---

    @Override
    public void deleteRole(long id) {
        if (!roleRepository.existsById(id)) {
            throw new IllegalArgumentException("Role is not found with id: " + id);
        }

        roleRepository.deleteById(id);
        logger.info("Role with id {} was deleted.", id);
    }
    //---
}
