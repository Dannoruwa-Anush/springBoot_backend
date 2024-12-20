package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.request.SubCategoryRequestDTO;
import com.example.demo.dto.response.SubCategoryResponseDTO;
import com.example.demo.dto.response.getById.SubCategoryGetByIdResponseDTO;
import com.example.demo.entity.SubCategory;

@Service
public interface SubCategoryService {
    
    List<SubCategoryResponseDTO> getAllSubCategories();

    SubCategoryGetByIdResponseDTO getSubCategoryById(long id);

    SubCategoryResponseDTO saveSubCategory(SubCategoryRequestDTO subCategorySaveRequestDTO);

    SubCategoryResponseDTO updateSubCategory(long id, SubCategoryRequestDTO subCategorySaveRequestDTO);

    void deleteSubCategory(long id);

    //entity -> dto
    SubCategoryResponseDTO toSubCategoryResponseDTO(SubCategory subCategory);
}
