package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data // Generate Getter & Setters using Lombok
public class OrderBookResponseDTO {
    // DTO specifies exactly what fields should be serialized.

    private int quantity; // Quantity of the book
    private BookInOrderResponseDTO book;
    private double subTotal = 0; //SubTotal of each book = quantity * unitPrice
}
