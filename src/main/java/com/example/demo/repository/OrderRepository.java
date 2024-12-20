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

    // 1. find all orders by user(customer) id
    // SQL QUERY : SELECT * FROM orders WHERE user_id = inputUserId;
    // JPQL query
    @Query("SELECT o FROM Order o where o.user.id = :inputUserId")
    List<Order> findAllOrdersByUserId(@Param("inputUserId") Long inputUserId);

    // 2. find all orders by order status
    // SQL QUERY : SELECT * FROM orders WHERE status = inputOrderStatus;
    // JPQL query
    @Query("SELECT o FROM Order o where o.status = :inputOrderStatus")
    List<Order> findAllOrdersByOrderStatus(@Param("inputOrderStatus") OrderStatus inputOrderStatus);

    // 3. find all orders by order place date (compare only the date part) and status
    // SQL QUERY : SELECT * FROM orders WHERE createdAt = inputOrderPlacedDate AND status = inputOrderStatus;
    // JPQL query
    @Query("SELECT o FROM Order o WHERE FUNCTION('DATE', o.createdAt) = :inputOrderPlacedDate AND o.status = :inputOrderStatus")
    List<Order> findAllOrdersByDateAndStatus(@Param("inputOrderPlacedDate") LocalDate inputOrderPlacedDate, @Param("inputOrderStatus") OrderStatus inputOrderStatus);

    boolean existsByUserId(long userId);
}
