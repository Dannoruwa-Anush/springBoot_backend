package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data //generate Getters and Setters using Lombok
public class BookResponseDTO {
    // DTO specifies exactly what fields should be serialized.
    private Long id;
    private String title;
    private double unitPrice;
    private int qoh;
    private String coverImage; // path for coverImage
    
    private String authorName;
    private String categoryName;
    private String subCategoryName;
}
