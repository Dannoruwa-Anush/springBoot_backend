package com.example.demo.setup;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Role;
import com.example.demo.entity.UserPermission;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserPermissionRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    /*
     * CommandLineRunner is a simple mechanism for running code at application
     * startup. It provides a way to execute specific logic after the Spring
     * application context is loaded but before the application starts accepting
     * requests. This is particularly useful for tasks like initializing databases,
     * running setup code, or executing one-time jobs.
     */

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserPermissionRepository userPermissionRepository;

    // Helper method to create UserPermission if it doesn't exist
    private void createPermissionIfNotExist(String permissionName) {
        if (userPermissionRepository.findByUserPermissionName(permissionName).isEmpty()) {
            UserPermission permission = new UserPermission();
            permission.setUserPermissionName(permissionName);
            userPermissionRepository.save(permission);
        }
    }

    // Helper method to get userPermission by name
    private UserPermission getPermissionByName(String permissionName) {
        return userPermissionRepository.findByUserPermissionName(permissionName)
                .orElseThrow(() -> new RuntimeException("UserPermission not found: " + permissionName));
    }

    // Helper method to create role if it doesn't exist
    private void createRoleIfNotExists(String roleName, Set<String> permissionNames) {
        if (roleRepository.findByRoleName(roleName).isEmpty()) {
            Role role = new Role();
            role.setRoleName(roleName);
            Set<UserPermission> permissions = permissionNames.stream()
                    .map(this::getPermissionByName)
                    .collect(Collectors.toSet());
            role.setUserPermissions(permissions);
            roleRepository.save(role);
        }
    }

    @Override
    public void run(String... args) throws Exception {

        /*
         * We have four userPermissions : CREATE, VIEW, DELETE, UPDATE
         * We have four roles : Admin, Manager, Cashier, Customer
         * 
         * We are going to add these role to the userPermission table and assign these
         * roles to the Roles accordingly at the intitiation of this project
         */

        // Check and create permissions if they don't exist
        createPermissionIfNotExist("CREATE");
        createPermissionIfNotExist("VIEW");
        createPermissionIfNotExist("DELETE");
        createPermissionIfNotExist("UPDATE");

        // Check and create roles with the necessary permissions
        // 1. Admin role
        createRoleIfNotExists("Admin", Set.of("CREATE", "VIEW", "DELETE", "UPDATE"));

        // 2. Cashier role
        createRoleIfNotExists("Cashier", Set.of("CREATE", "VIEW"));

        // 3. Customer role
        createRoleIfNotExists("Customer", Set.of("CREATE", "VIEW"));

        // 4. Manager role
        createRoleIfNotExists("Manager", Set.of("CREATE", "VIEW", "UPDATE"));
    }
}
