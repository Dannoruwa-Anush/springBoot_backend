package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.request.saveRequest.SubCategorySaveRequestDTO;
import com.example.demo.entity.SubCategory;

@Service
public interface SubCategoryService {
    
    List<SubCategory> getAllSubCategories();

    SubCategory getSubCategoryById(long id);

    SubCategory saveSubCategory(SubCategorySaveRequestDTO subCategorySaveRequestDTO);

    SubCategory updateSubCategory(long id, SubCategorySaveRequestDTO subCategorySaveRequestDTO);

    void deleteSubCategory(long id);
}
