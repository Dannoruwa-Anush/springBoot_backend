package com.example.demo.service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.request.RoleRequestDTO;
import com.example.demo.entity.Role;
import com.example.demo.entity.UserPermission;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserPermissionRepository;

import jakarta.transaction.Transactional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserPermissionRepository userPermissionRepository;

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role getRoleById(long id) {
        return roleRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Role is not found " + id));
    }

    @Override
    public Role saveRole(Role role) {
        Optional<Role> existingRole = roleRepository.findByRoleName(role.getRoleName());
        if (existingRole.isPresent()) {
            throw new IllegalArgumentException("Role with name '" + role.getRoleName() + "' already exists.");
        }
        return roleRepository.save(role);
    }

    @Override
    public Role updateRole(long id, Role role) {
        Role existingRole = getRoleById(id);
        existingRole.setRoleName(role.getRoleName());

        return roleRepository.save(existingRole);
    }

    @Override
    public void deleteRole(long id) {
        roleRepository.deleteById(id);
    }

    // role - userPermission Assignment

    @Override
    public Role saveOrUpdateRoleWithPermissions(RoleRequestDTO roleRequest) {
        Role createOrUpdateRole;

        // Determine whether to update an existing role or create a new one
        if (roleRequest.getRoleId() != null) {
            // Fetch the existing role to update
            createOrUpdateRole = getRoleById(roleRequest.getRoleId());
        } else {
            // Create a new role if no ID is provided
            createOrUpdateRole = new Role();
            createOrUpdateRole.setId(null); //DB will set this automatically
            createOrUpdateRole.setRoleName(roleRequest.getRoleName()); // Ensure roleName is set
        }

        // Fetch and set permissions
        List<UserPermission> permissions = userPermissionRepository.findAllById(roleRequest.getPermissionIds());

        // Use a temporary set to safely handle permissions
        Set<UserPermission> updatedPermissions = new HashSet<>(permissions);

        // Update the role's permissions
        createOrUpdateRole.setUserPermissions(updatedPermissions);

        // Save and return the updated or newly created role
        return roleRepository.save(createOrUpdateRole);
    }

    @Override
    public Role addPermissionsToExistingRole(RoleRequestDTO roleRequest) {
        // Fetch the Role entity by its ID
        Role role = getRoleById(roleRequest.getRoleId());

        // Fetch the UserPermission entities by their IDs
        List<UserPermission> permissions = userPermissionRepository.findAllById(roleRequest.getPermissionIds());

        // Use a temporary collection to avoid ConcurrentModificationException
        Set<UserPermission> existingPermissions = new HashSet<>(role.getUserPermissions());

        // Add all permissions to the temporary collection
        existingPermissions.addAll(permissions);

        // Replace the original collection with the updated one
        role.setUserPermissions(existingPermissions);

        // Save and return the updated Role
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public Role removePermissionsFromExistingRole(RoleRequestDTO roleRequest) {
        Role role = getRoleById(roleRequest.getRoleId());

        // Collect permissions to remove
        Set<UserPermission> permissionsToRemove = new HashSet<>();

        for (UserPermission permission : role.getUserPermissions()) {
            if (roleRequest.getPermissionIds().contains(permission.getId())) {
                permissionsToRemove.add(permission);
            }
        }

        // Remove collected permissions
        role.getUserPermissions().removeAll(permissionsToRemove);

        // Save the updated role
        return roleRepository.save(role);
    }

    @Override
    public Role getRoleByName(String roleName) {
        return roleRepository.findByRoleName(roleName).orElseThrow(() -> new NoSuchElementException(roleName +" is not found with role name"));
    }
}
