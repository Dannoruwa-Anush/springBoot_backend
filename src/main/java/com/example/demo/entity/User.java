package com.example.demo.entity;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users") // set table name
@NoArgsConstructor
@AllArgsConstructor
@Data // generate Getters and Setters using Lombok
public class User {
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

   @Column(unique = true, nullable = false)
   private String username;

   @Email // validate email addresses
   @Column(unique = true, nullable = false)
   private String email;

   @Column(nullable = false)
   private String password;

   @Column(nullable = false)
   private String address;

   @Column(length = 15)
   @Size(min = 10)
   private String telephoneNumber;

   @Column(name = "first_login", nullable = false)
   private boolean firstLogin = true;

   /*
    * LAZY: This means that the related entities (in the Many-to-Many relationship)
    * will not be fetched immediately when the parent entity is loaded.
    */
   // User (Many) --- (Many) Role
   // User side relationship
   @ManyToMany(fetch = FetchType.LAZY)
   @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
   private Set<Role> roles;

   /*
    * Parent Entity: The entity on the "one" side of the relationship.
    * Child Entity: The entity on the "many" side of the relationship.
    * 
    * 
    * cascade = CascadeType.ALL:
    * when you perform an operation on the parent entity, that operation will also
    * be applied to the child entities (those referenced by the relationship).
    * 
    * 
    * orphanRemoval = true
    * the child entity becomes an "orphan," and if it's no longer associated with
    * any parent, it is removed from the database.
    */

   // User (One) --- (Many) Order
   // User side relationship
   /* "user" -> variable "private User user;" in Order.java */
   @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
   private List<Order> orders;

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
      User user = (User) obj;
      return Objects.equals(id, user.id);
   }
}
