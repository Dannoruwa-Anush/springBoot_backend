package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
    //JpaRepository is a generic interface, meaning it can work with any entity class and its corresponding ID type
    //It extends CrudRepository and PagingAndSortingRepository, which means it inherits methods for basic CRUD operations (like save, find, delete), as well as methods for pagination and sorting.
    
    //We can add custom queries here.
    
    Optional<Role> findByRoleName(String roleName);
}
