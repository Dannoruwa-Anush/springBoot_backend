package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.request.SubCategoryRequestDTO;
import com.example.demo.dto.response.SubCategoryResponseDTO;

@Service
public interface SubCategoryService {
    
    List<SubCategoryResponseDTO> getAllSubCategories();

    SubCategoryResponseDTO getSubCategoryById(long id);

    SubCategoryResponseDTO saveSubCategory(SubCategoryRequestDTO subCategorySaveRequestDTO);

    SubCategoryResponseDTO updateSubCategory(long id, SubCategoryRequestDTO subCategorySaveRequestDTO);

    void deleteSubCategory(long id);
}
