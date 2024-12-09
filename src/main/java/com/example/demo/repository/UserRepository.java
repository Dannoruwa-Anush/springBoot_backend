package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // JpaRepository is a generic interface, meaning it can work with any entity
    // class and its corresponding ID type
    // It extends CrudRepository and PagingAndSortingRepository, which means it
    // inherits methods for basic CRUD operations (like save, find, delete), as well
    // as methods for pagination and sorting.

    // We can add custom queries here.

    // 1.find user by username
    Optional<User> findByUsername(String username); // optional -> Avoid NullPointerException,
                                                    // if no user with that username exists,
                                                    // the result is an empty container rather than returning null.
    
    // 2.exists by username
    Boolean existsByUsername(String username);

    // 3.exists by email
    Boolean existsByEmail(String email);
}
