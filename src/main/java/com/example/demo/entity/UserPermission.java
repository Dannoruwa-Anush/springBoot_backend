package com.example.demo.entity;

import java.util.Objects;
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
@Table(name = "userpermissions") // set table name
@NoArgsConstructor
@AllArgsConstructor
@Data // generate Getters and Setters using Lombok
public class UserPermission {
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
    private String userPermissionName;

    // Role (Many) --- (Many) UserPermission
    // UserPermission side relationship
    /*
     * userPermissions -> variable "private Set<UserPermission> userPermissions;" in
     * the Role.
     */
    @ManyToMany(mappedBy = "userPermissions")
    private Set<Role> roles;

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
        UserPermission userPermission = (UserPermission) obj;
        return Objects.equals(id, userPermission.id);
    }
}
