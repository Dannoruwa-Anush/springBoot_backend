package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.common.projectEnum.OrderStatus;
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

    // 1. find all orders by user(customer) id (desending)
    // SQL QUERY : SELECT * FROM orders WHERE user_id = inputUserId ORDER BY createdAt DESC;
    // JPQL query
    @Query("SELECT o FROM Order o where o.user.id = :inputUserId ORDER BY o.createdAt DESC")
    List<Order> findAllOrdersByUserId(@Param("inputUserId") Long inputUserId);

    // 2. find all orders by order status (desending)
    // SQL QUERY : SELECT * FROM orders WHERE status = inputOrderStatus ORDER BY createdAt DESC;
    // JPQL query
    @Query("SELECT o FROM Order o where o.status = :inputOrderStatus ORDER BY o.createdAt DESC")
    List<Order> findAllOrdersByOrderStatus(@Param("inputOrderStatus") OrderStatus inputOrderStatus);

    // 3. find all orders by order place date (compare only the date part) and status (desending)
    // SQL QUERY : SELECT * FROM orders WHERE createdAt = inputOrderPlacedDate AND status = inputOrderStatus ORDER BY createdAt DESC;
    // JPQL query
    @Query("SELECT o FROM Order o WHERE FUNCTION('DATE', o.createdAt) = :inputOrderPlacedDate AND o.status = :inputOrderStatus ORDER BY o.createdAt DESC")
    List<Order> findAllOrdersByDateAndStatus(@Param("inputOrderPlacedDate") LocalDate inputOrderPlacedDate, @Param("inputOrderStatus") OrderStatus inputOrderStatus);

    boolean existsByUserId(long userId);
}
