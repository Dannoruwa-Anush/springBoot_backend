package com.example.demo.dto.response;

import java.time.LocalDate;

import com.example.demo.common.projectEnum.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data // Generate Getter & Setters using Lombok
public class OrderResponseDTO {
    // DTO specifies exactly what fields should be serialized.

    private Long id; 
    private double totalAmount;
    private LocalDate createdAt;
    private OrderStatus status;

    private String customerName;
}
