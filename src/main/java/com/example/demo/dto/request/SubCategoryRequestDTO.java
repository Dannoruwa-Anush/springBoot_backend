package com.example.demo.dto.request;

import lombok.Data;

@Data //Generate Getter & Setters using Lombok
public class SubCategoryRequestDTO {
    //DTO specifies exactly what fields should be serialized.
    private String subCategoryName;
    private Long categoryId;
}
