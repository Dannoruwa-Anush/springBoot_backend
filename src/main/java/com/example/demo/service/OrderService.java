package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.request.OrderByDateRequestDTO;
import com.example.demo.dto.request.OrderByStatusRequestDTO;
import com.example.demo.dto.request.OrderRequestDTO;
import com.example.demo.dto.request.OrderStatusUpdateRequestDTO;
import com.example.demo.dto.request.ShoppingCartTotalRequestDTO;
import com.example.demo.dto.response.OrderBillResponseDTO;
import com.example.demo.dto.response.OrderResponseDTO;
import com.example.demo.dto.response.ShoppingCartTotalResponseDTO;

@Service
public interface OrderService {
    List<OrderResponseDTO> getAllOrders();
    OrderBillResponseDTO getOrderById(long id);
    OrderResponseDTO saveOrder(OrderRequestDTO orderRequestDTO);
    OrderResponseDTO updateOrder(long id, OrderRequestDTO orderRequestDTO);
    void deleteOrder(long id);  

    List<OrderResponseDTO> getAllOrdersByUserId(long inputUserId);
    List<OrderResponseDTO> getAllOrdersByOrderStatus(OrderByStatusRequestDTO orderByStatusRequestDTO);
    List<OrderResponseDTO> findAllOrdersByDateAndStatus(OrderByDateRequestDTO orderByDateRequestDTO);

    OrderResponseDTO updateOrderStatus(long id, OrderStatusUpdateRequestDTO orderStatusUpdateRequestDTO);

    ShoppingCartTotalResponseDTO calculateTotalAmount(ShoppingCartTotalRequestDTO ShoppingCartTotalRequestDTO);
}
