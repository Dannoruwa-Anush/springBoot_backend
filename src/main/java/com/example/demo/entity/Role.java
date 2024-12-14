package com.example.demo.entity;

import java.util.Objects;
import java.util.Set;

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
@Table(name = "roles") // set table name
@NoArgsConstructor
@AllArgsConstructor
@Data // generate Getters and Setters using Lombok
public class Role {
    // Define columns in table

    /*
     * @JsonIgnore : prevent this attribute from being included in the JSON output.
     * private DataType attributeName;
     * 
     * Don't use @JsonIgnore in the entity level beacuse it's better to isolate the
     * serialization concerns from the entity logic, which is where DTOs help.
     * 
     * DTO specifies exactly what fields should be serialized.
     */

    /*
     * @GeneratedValue
     * generate the primary key value by the database itself using the
     * auto-increment column option
     */
    @Id // set primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // primary key

    @Column(nullable = false, unique = true)
    private String roleName;

    // User (Many) --- (Many) Role
    // Role side relationship
    // roles -> variable "private Set<Role> roles;" in the User.java
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    // Role (Many) --- (Many) UserPermission
    // Role side relationship
    /*
     * LAZY: This means that the related entities (in the Many-to-Many relationship)
     * will not be fetched immediately when the parent entity is loaded.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "role_userPermissions", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "userPermission_id"))
    private Set<UserPermission> userPermissions;

    /*
     * List is an ordered collection that allows duplicate elements.
     * Set is an unordered collection that does not allow duplicate elements
     * HashSet uses a hash table to store the elements, making it efficient for
     * operations
     * 
     * 
     * In parent class(Object) :
     * hashCode()
     * This method returns the default hash code of the object, which is derived
     * from the object's memory address
     * 
     * equals()
     * objects with the same data might be treated as different due to their
     * different hash codes
     * 
     * 
     * In child class(@Override) :
     * hashCode()
     * ensures that the hash code is based on the value of the id field, not the
     * object's address.
     * 
     * equals()
     * compares the id field to determine object equality.
     */
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
        Role role = (Role) obj;
        return Objects.equals(id, role.id);
    }
}
