package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.request.RoleRequestDTO;
import com.example.demo.entity.Role;

@Service
public interface RoleService {

    List<Role> getAllRoles();
    Role getRoleById(long id);
    Role saveRole(Role role);
    Role updateRole(long id, Role role);
    void deleteRole(long id);  

    //role - userPermission Assignment
    Role saveOrUpdateRoleWithPermissions(RoleRequestDTO roleRequest);
    Role addPermissionsToExistingRole(RoleRequestDTO roleRequest);
    Role removePermissionsFromExistingRole(RoleRequestDTO roleRequest);

    Role getRoleByName(String roleName);
}
