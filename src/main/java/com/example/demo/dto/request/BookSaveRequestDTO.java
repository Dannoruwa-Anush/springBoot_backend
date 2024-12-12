package com.example.demo.dto.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class BookSaveRequestDTO {
    
    private String title;
    private double unitPrice;
    private MultipartFile coverImage; //MultipartFile file: This is the object that will hold the file data.
}
