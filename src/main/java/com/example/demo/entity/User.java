package com.example.demo.entity;

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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data //generate Getters and Setters using Lombok
public class User {
   //Define columns in table
   
   @Id //set primary key
   @GeneratedValue(strategy = GenerationType.IDENTITY) //generate the primary key value by the database itself using the auto-increment column option
   private Long id; //primary key

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

   //User (Many) ---  (Many) Role
   //User side relationship
   @ManyToMany(fetch = FetchType.EAGER) //Eager: This means that the related entities (in the Many-to-Many relationship) will be fetched immediately when the parent entity is loaded. 
   @JoinTable(
      name = "user_roles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id")
   )
   private Set<Role> roles;
}
