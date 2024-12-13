package com.example.demo.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.customHttpResponse.ErrorResponse;
import com.example.demo.dto.request.saveRequest.SubCategorySaveRequestDTO;
import com.example.demo.entity.SubCategory;
import com.example.demo.service.SubCategoryService;

@RestController
@RequestMapping("subCategory")
public class SubCategoryController {

    @Autowired
    private SubCategoryService subCategoryService;

    /*
     * ResponseEntity is a powerful class in Spring Boot for managing HTTP
     * responses.
     * 
     * It allows you to:
     * 
     * Return custom status codes.
     * Add headers.
     * Set the body of the response.
     * 
     * .build() - You typically use .build() when you want to send an HTTP status
     * without any associated content in the response body.
     */

     //---

    @GetMapping
    public ResponseEntity<List<SubCategory>> getAllSubCategories() {
        List<SubCategory> subCategories = subCategoryService.getAllSubCategories();

        if (subCategories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status((HttpStatus.OK)).body(subCategories);
    }
    // ---

    @GetMapping("/{id}")
    public ResponseEntity<SubCategory> getAllSubCategoryById(@PathVariable Long id) {
        try {
            SubCategory subCategory = subCategoryService.getSubCategoryById(id);
            return ResponseEntity.status(HttpStatus.OK).body(subCategory);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
    }
    // ---

    @PostMapping
    public ResponseEntity<Object> createSubCategory(@RequestBody SubCategorySaveRequestDTO subCategorySaveRequestDTO) {
        
        try {
            SubCategory savedSubCategory = subCategoryService.saveSubCategory(subCategorySaveRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedSubCategory);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    // ---

    @PutMapping("{id}")
    public ResponseEntity<Object> updateSubCategory(@PathVariable Long id, @RequestBody SubCategorySaveRequestDTO subCategorySaveRequestDTO) {
        try {
            SubCategory updatedSubCategory = subCategoryService.updateSubCategory(id, subCategorySaveRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body(updatedSubCategory);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sub category is not found");
        }
    }
    // ---

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteSubCategory(@PathVariable Long id) {
        try {
            subCategoryService.deleteSubCategory(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sub category is not found");
        }
    }
    // ---
}
