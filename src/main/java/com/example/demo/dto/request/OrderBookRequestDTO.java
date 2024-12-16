package com.example.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data // generate Getters and Setters using Lombok
public class OrderBookRequestDTO {
    //DTO specifies exactly what fields should be serialized.
    private Long bookId;
    private int quantity; // Quantity for this book
}
