package com.example.demo.service;

import java.util.Date;
import java.util.List;

import com.example.demo.common.projectEnum.OrderStatus;
import com.example.demo.entity.Order;

public interface OrderService {
    List<Order> getAllOrders();
    Order getOrderById(long id);
    Order saveOrder(Order order);
    Order updateOrder(long id, Order order);
    void deleteOrder(long id);  

    List<Order> getAllOrdersByUserId(Long inputUserId);
    List<Order> getAllOrdersByOrderStatus(OrderStatus inputOrderStatus);
    List<Order> getAllOrdersByOrderPlaceDate(Date inputOrderPlacedDate);
}