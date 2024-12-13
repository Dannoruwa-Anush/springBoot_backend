package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data //generate Getters and Setters using Lombok
public class BookResponseDTO {

    private Long id;
    private String title;
    private double unitPrice;
    private int qoh;
    
    private String authorName;
}
