package com.example.demo.dto.response;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data //generate Getters and Setters using Lombok
public class CategoryWithSubCategoryResponseDTO {
    //DTO specifies exactly what fields should be serialized.
    private Long id;
    private String categoryName;
    private Set<String> subCategoriesNames;   
}