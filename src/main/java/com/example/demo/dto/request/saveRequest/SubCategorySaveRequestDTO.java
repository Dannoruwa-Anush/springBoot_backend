package com.example.demo.dto.request.saveRequest;

import lombok.Data;

@Data //Generate Getter & Setters using Lombok
public class SubCategorySaveRequestDTO {
    
    private String subCategoryName;
    private Long categoryId;
}
