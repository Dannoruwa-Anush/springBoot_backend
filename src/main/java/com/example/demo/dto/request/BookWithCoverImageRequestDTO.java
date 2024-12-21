package com.example.demo.dto.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data // generate Getters and Setters using Lombok
public class BookWithCoverImageRequestDTO {
    // DTO specifies exactly what fields should be serialized.
    private String title;
    private double unitPrice;
    private int qoh;
    private MultipartFile coverImage; //MultipartFile file: This is the object that will hold the file data.
    
    private Long subCategoryId;
    private Long authorId;
}
