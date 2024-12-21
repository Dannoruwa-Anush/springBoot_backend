package com.example.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data // generate Getters and Setters using Lombok
public class BookWithOutCoverImageRequestDTO {
    // DTO specifies exactly what fields should be serialized.
    private String title;
    private double unitPrice;
    private int qoh;
    
    private Long subCategoryId;
    private Long authorId;
}
