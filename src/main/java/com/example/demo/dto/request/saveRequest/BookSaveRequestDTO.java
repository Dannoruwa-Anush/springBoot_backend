package com.example.demo.dto.request.saveRequest;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class BookSaveRequestDTO {
    
    private String title;
    private double unitPrice;
    private int qoh;
    private MultipartFile coverImage; //MultipartFile file: This is the object that will hold the file data.
    
    private Long subCategoryId;
    private Long authorId;
}
