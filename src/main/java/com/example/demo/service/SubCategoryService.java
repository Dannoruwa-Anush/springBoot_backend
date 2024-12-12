package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.SubCategory;

@Service
public interface SubCategoryService {
    
    List<SubCategory> getAllSubCategories();

    SubCategory getSubCategoryById(long id);

    SubCategory saveSubCategory(SubCategory subCategory);

    SubCategory updateSubCategory(long id, SubCategory subCategory);

    void deleteSubCategory(long id);
}
