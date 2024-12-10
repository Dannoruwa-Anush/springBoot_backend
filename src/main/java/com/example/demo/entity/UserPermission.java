package com.example.demo.entity;

import java.util.Set;

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
@Table(name = "userpermissions") //set table name
@NoArgsConstructor
@AllArgsConstructor
@Data //generate Getters and Setters using Lombok
public class UserPermission {

    @Id //set primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //generate the primary key value by the database itself using the auto-increment column option
    private Long id; //primary key
    
    @Column(nullable = false, unique = true)
    private String userPermissionName;

    //Role (Many) ---  (Many) UserPermission
    //UserPermission side relationship
    @ManyToMany(mappedBy = "userPermissions") //userPermissions -> variable "private Set<UserPermission> userPermissions;" in the Role.java
    private Set<Role> roles;
}
