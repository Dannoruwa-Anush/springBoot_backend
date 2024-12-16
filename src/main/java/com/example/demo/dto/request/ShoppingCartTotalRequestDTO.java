package com.example.demo.dto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data // generate Getters and Setters using Lombok
public class ShoppingCartTotalRequestDTO {
    // DTO specifies exactly what fields should be serialized.
    List<OrderBookRequestDTO> shoppingCartBooks;
}
