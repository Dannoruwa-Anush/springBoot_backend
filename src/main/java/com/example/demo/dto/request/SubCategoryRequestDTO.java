package com.example.demo.dto.request;

import lombok.Data;

@Data //Generate Getter & Setters using Lombok
public class SubCategoryRequestDTO {
    
    private String subCategoryName;
    private Long categoryId;
}
