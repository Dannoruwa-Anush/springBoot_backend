package com.example.demo.dto.request;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data //generate Getters and Setters using Lombok
public class OrderRequestDTO {
    
    private double totalAmount;

    private Set<Long> bookIds;
    private Long customerId; 
}
