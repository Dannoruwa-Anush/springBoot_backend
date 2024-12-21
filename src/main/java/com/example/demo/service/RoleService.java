package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.request.RoleRequestDTO;
import com.example.demo.dto.response.RoleResponseDTO;
import com.example.demo.dto.response.StaffRoleResponseDTO;

@Service
public interface RoleService {

    List<RoleResponseDTO> getAllRoles();
    RoleResponseDTO getRoleById(long id);
    RoleResponseDTO saveRole(RoleRequestDTO roleRequestDTO);
    RoleResponseDTO updateRole(long id, RoleRequestDTO roleRequestDTO);
    void deleteRole(long id);  

    List<StaffRoleResponseDTO> getAllStaffRoles();
}
