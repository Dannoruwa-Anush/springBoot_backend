package com.example.demo.dto.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class BookRequestDTO {
    
    private String title;
    private double unitPrice;
    private int qoh;
    private MultipartFile coverImage; //MultipartFile file: This is the object that will hold the file data.
    
    private Long subCategoryId;
    private Long authorId;
}
