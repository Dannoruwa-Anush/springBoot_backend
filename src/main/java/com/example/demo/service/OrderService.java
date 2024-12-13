package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.common.projectEnum.OrderStatus;
import com.example.demo.dto.request.OrderRequestDTO;
import com.example.demo.entity.Order;

@Service
public interface OrderService {
    List<Order> getAllOrders();
    Order getOrderById(long id);
    Order saveOrder(OrderRequestDTO orderRequestDTO);
    Order updateOrder(long id, OrderRequestDTO orderRequestDTO);
    void deleteOrder(long id);  

    List<Order> getAllOrdersByUserId(Long inputUserId);
    List<Order> getAllOrdersByOrderStatus(OrderStatus inputOrderStatus);
    List<Order> findAllOrdersByDateAndStatus(LocalDate inputOrderPlacedDate, OrderStatus inputOrderStatus);

    Order updateOrderStatus(long id, OrderStatus newStatus);
}
