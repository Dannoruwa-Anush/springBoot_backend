package com.example.demo.entity;

import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")//set table name
@NoArgsConstructor
@AllArgsConstructor
@Data //generate Getters and Setters using Lombok
public class Role {

    @Id //set primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //generate the primary key value by the database itself using the auto-increment column option
    private Long id; //primary key

    @Column(nullable = false, unique = true)
    private String roleName;

    //User (Many) ---  (Many) Role
    //Role side relationship
    @ManyToMany(mappedBy = "roles") //roles -> variable "private Set<Role> roles;" in the User.java
    @JsonIgnore //prevent this property from being included in the JSON output.
    private Set<User> users;

    //Role (Many) ---  (Many) UserPermission
    //Role side relationship
    @ManyToMany(fetch = FetchType.LAZY) //LAZY: This means that the related entities (in the Many-to-Many relationship) will not be fetched immediately when the parent entity is loaded. 
    @JoinTable(
        name = "role_userPermissions",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "userPermission_id")
    )
    @JsonIgnore //prevent this property from being included in the JSON output.
    private Set<UserPermission> userPermissions;
    //EAGER : When You Always Need the Related Data
    //LAZY  : When You Avoid Unnecessary Data Loading

    // List is an ordered collection that allows duplicate elements.
    
    // Set is an unordered collection that does not allow duplicate elements

    // HashSet uses a hash table to store the elements, making it efficient for
    // operations














    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Role role = (Role) obj;
        return Objects.equals(id, role.id);
    }
}
