package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.request.CategoryRequestDTO;
import com.example.demo.dto.response.CategoryResponseDTO;

@Service
public interface CategoryService {

    List<CategoryResponseDTO> getAllCategories();

    CategoryResponseDTO getCategoryById(long id);

    CategoryResponseDTO saveCategory(CategoryRequestDTO categoryRequestDTO);

    CategoryResponseDTO updateCategory(long id, CategoryRequestDTO categoryRequestDTO);

    void deleteCategory(long id);
}
