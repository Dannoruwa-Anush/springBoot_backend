package com.example.demo.dto.response.getById;

import com.example.demo.dto.response.AuthorResponseDTO;
import com.example.demo.dto.response.SubCategoryResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data //generate Getters and Setters using Lombok
public class BooKGetByIdResponseDTO {
    // DTO specifies exactly what fields should be serialized.
    private Long id;
    private String title;
    private double unitPrice;
    private int qoh;
    private String coverImage; // path for coverImage

    private AuthorResponseDTO author;
    private SubCategoryResponseDTO subCategory;
}
