package com.example.demo.entity;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

   @Id // set primary key
   @GeneratedValue(strategy = GenerationType.IDENTITY) // generate the primary key value by the database itself using
                                                       // the auto-increment column option
   private Long id; // primary key

   @Column(unique = true, nullable = false)
   private String username;

   @Column(unique = true, nullable = false)
   private String email;

   @Column(nullable = false)
   private String password;

   @Column(nullable = false)
   private String address;

   @Column(length = 15)
   private String telephoneNumber;

   @Column(name = "first_login", nullable = false)
   private boolean firstLogin = true;

   // User (Many) --- (Many) Role
   // User side relationship
   @ManyToMany(fetch = FetchType.LAZY) // LAZY: This means that the related entities (in the Many-to-Many relationship)
                                       // will not be fetched immediately when the parent entity is loaded.
   @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
   @JsonIgnore // prevent this property from being included in the JSON output.
   private Set<Role> roles;

   // User (One) --- (Many) Order
   // User side relationship
   @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) // "user" -> variable "private User
                                                                                  // user;" in Order.java
   @JsonIgnore // prevent this property from being included in the JSON output.
   private List<Order> orders;
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
