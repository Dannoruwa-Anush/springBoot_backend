package com.example.demo.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.example.demo.common.projectEnum.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data // Generate Getter & Setters using Lombok
public class OrderBillResponseDTO {
    // DTO specifies exactly what fields should be serialized.

    private Long id; 
    private double totalAmount;
    private LocalDate createdAt;
    private OrderStatus status;

    //user(customer) details
    private CustomerUserResponseDTO customer;
    
    //Order details
    List<OrderBookResponseDTO> orderBooks;
}
