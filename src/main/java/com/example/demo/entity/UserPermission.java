package com.example.demo.entity;

import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "userpermissions") // set table name
@NoArgsConstructor
@AllArgsConstructor
@Data // generate Getters and Setters using Lombok
public class UserPermission {

    @Id // set primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // generate the primary key value by the database itself using
                                                        // the auto-increment column option
    private Long id; // primary key

    @Column(nullable = false, unique = true)
    private String userPermissionName;

    // Role (Many) --- (Many) UserPermission
    // UserPermission side relationship
    @ManyToMany(mappedBy = "userPermissions") // userPermissions -> variable "private Set<UserPermission>
                                              // userPermissions;" in the Role.
    @JsonIgnore //prevent this property from being included in the JSON output.
    private Set<Role> roles;
    // EAGER : When You Always Need the Related Data
    // LAZY : When You Avoid Unnecessary Data


















    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        UserPermission userPermission = (UserPermission) obj;
        return Objects.equals(id, userPermission.id);
    }
}
