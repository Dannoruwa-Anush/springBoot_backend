package com.example.demo.dto.response.getById;

import com.example.demo.dto.response.CategoryResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data //generate Getters and Setters using Lombok
public class SubCategoryGetByIdResponseDTO {
    // DTO specifies exactly what fields should be serialized.
    private Long id;
    private String subCategoryName;
    private CategoryResponseDTO Category;
}
