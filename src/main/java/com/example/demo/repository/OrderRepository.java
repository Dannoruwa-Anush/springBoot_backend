package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    /*
     * JpaRepository is a generic interface, meaning it can work with any entity
     * class and its corresponding ID type
     * It extends CrudRepository and PagingAndSortingRepository, which means it
     * inherits methods for basic CRUD operations (like save, find, delete), as well
     * as methods for pagination and sorting.
     */

    // We can add custom queries here.
}
