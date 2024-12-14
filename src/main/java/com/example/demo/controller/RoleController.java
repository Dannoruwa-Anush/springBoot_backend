package com.example.demo.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.customHttpResponse.CustomErrorResponse;
import com.example.demo.dto.request.RoleRequestDTO;
import com.example.demo.dto.response.RoleDTO;
import com.example.demo.dto.response.RoleResponseDTO;
import com.example.demo.entity.Role;
import com.example.demo.service.RoleService;

@RestController
@RequestMapping("roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    private RoleDTO toDTO(Role role) {
        RoleDTO dto = new RoleDTO();
        dto.setId(role.getId());
        dto.setRoleName(role.getRoleName());
        return dto;
    }

    private Role toEntity(RoleDTO dto) {
        Role role = new Role();
        role.setId(dto.getId());
        role.setRoleName(dto.getRoleName());
        return role;
    }

    private RoleResponseDTO toRoleResponseDTO(Role role){
        RoleResponseDTO responseDto = new RoleResponseDTO();
        responseDto.setRoleId(role.getId());
        responseDto.setRoleName(role.getRoleName());
        responseDto.setUserPermissions(role.getUserPermissions());
        return responseDto;
    } 

    /*
     * ResponseEntity is a powerful class in Spring Boot for managing HTTP
     * responses.
     * 
     * It allows you to:
     * 
     * Return custom status codes.
     * Add headers.
     * Set the body of the response.
     * 
     * .build() - You typically use .build() when you want to send an HTTP status
     * without any associated content in the response body.
     */

    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();

        if(roles.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        List<RoleDTO> roleDTOs = roles.stream().map(this::toDTO).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(roleDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable long id) {
        try {
            Role role = roleService.getRoleById(id);
            return ResponseEntity.status(HttpStatus.OK).body(toDTO(role));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<Object> createRole(@RequestBody RoleDTO roleDTO) {
        try {
            Role role = toEntity(roleDTO);
            Role createdRole = roleService.saveRole(role);
            return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(createdRole));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomErrorResponse(e.getMessage()));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateRole(@PathVariable long id, @RequestBody RoleDTO roleDTO) {
        try {
            Role role = toEntity(roleDTO);
            Role updatedRole = roleService.updateRole(id, role);
            return ResponseEntity.status(HttpStatus.OK).body(toDTO(updatedRole));
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomErrorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role is not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteRole(@PathVariable long id) {
        try {
            roleService.deleteRole(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomErrorResponse(e.getMessage()));
        }catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role is not found");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    //-- APIs related to role - userPermission Assignment
    @PostMapping("saveOrUpdateRoleWithPermissions")
    public ResponseEntity<RoleResponseDTO> saveOrUpdateRoleWithPermissions(@RequestBody RoleRequestDTO roleRequest) {
        try {
            Role createdRole = roleService.saveOrUpdateRoleWithPermissions(roleRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(toRoleResponseDTO(createdRole));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("addPermissionsToExistingRole")
    public ResponseEntity<RoleResponseDTO> addPermissionsToExistingRole(@RequestBody RoleRequestDTO roleRequest) {
        try {
            Role createdRole = roleService.addPermissionsToExistingRole(roleRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(toRoleResponseDTO(createdRole));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("removePermissionsFromExistingRole")
    public ResponseEntity<RoleResponseDTO> removePermissionsFromExistingRole(@RequestBody RoleRequestDTO roleRequest) {
        try {
            Role createdRole = roleService.removePermissionsFromExistingRole(roleRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(toRoleResponseDTO(createdRole));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
