package com.example.demo.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Role;
import com.example.demo.repository.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService{

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role getRoleById(long id) {
        return roleRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Role is not found" + id));
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
    
}
