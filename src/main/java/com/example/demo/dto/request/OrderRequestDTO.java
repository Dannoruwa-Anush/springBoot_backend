package com.example.demo.dto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data //generate Getters and Setters using Lombok
public class OrderRequestDTO {
    //DTO specifies exactly what fields should be serialized.
    private List<OrderBookRequestDTO> books;
    private Long customerId; 
}
