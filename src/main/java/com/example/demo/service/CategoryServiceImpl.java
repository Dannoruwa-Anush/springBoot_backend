package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.request.CategoryRequestDTO;
import com.example.demo.dto.response.CategoryResponseDTO;
import com.example.demo.dto.response.CategoryWithSubCategoryResponseDTO;
import com.example.demo.dto.response.SubCategoryFilterResponseDTO;
import com.example.demo.entity.Category;
import com.example.demo.entity.SubCategory;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.SubCategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    // include Logger(slf4j) for auditing purposes
    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    // ********
    //This DTO will use in other services as well
    @Override
    public CategoryResponseDTO toCategoryResponseDTO(Category category) {
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(category.getId());
        dto.setCategoryName(category.getCategoryName());
        return dto;
    }
    // ---

    //----
    private List<SubCategoryFilterResponseDTO> toSubCategoryFilterResponseDTO(List<SubCategory> subCategories){
        List<SubCategoryFilterResponseDTO> listDTOs = new ArrayList<>();

        for(SubCategory subCategory : subCategories){
            SubCategoryFilterResponseDTO dto = new SubCategoryFilterResponseDTO();
            dto.setId(subCategory.getId());
            dto.setSubCategoryName(subCategory.getSubCategoryName());

            listDTOs.add(dto);
        }
        return listDTOs;
    }
    //----

    private CategoryWithSubCategoryResponseDTO toCategoryWithSubCategoryResponseDTO(Category category) {
        CategoryWithSubCategoryResponseDTO dto = new CategoryWithSubCategoryResponseDTO();
        dto.setId(category.getId());
        dto.setCategoryName(category.getCategoryName());
        dto.setSubCategories(toSubCategoryFilterResponseDTO(category.getSubcategories()));
        return dto;
    }
    // ********

    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponseDTO> categoriesDTOs = categories.stream().map(this::toCategoryResponseDTO)
                .collect(Collectors.toList());
        return categoriesDTOs;
    }
    // ---

    @Override
    public CategoryResponseDTO getCategoryById(long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Category is not found with id: " + id));

        return toCategoryResponseDTO(category);
    }
    // ---

    @Override
    public CategoryResponseDTO saveCategory(CategoryRequestDTO categoryRequestDTO) {
        Optional<Category> CategoryWithSameName = categoryRepository
                .findByCategoryName(categoryRequestDTO.getCategoryName());

        if (CategoryWithSameName.isPresent()) {
            throw new IllegalArgumentException(
                    "Category with name " + categoryRequestDTO.getCategoryName() + " already exists");
        }

        //create new category
        Category saveTocategory = new Category();
        saveTocategory.setCategoryName(categoryRequestDTO.getCategoryName());
        return toCategoryResponseDTO(categoryRepository.save(saveTocategory));
    }
    // ---

    @Override
    public CategoryResponseDTO updateCategory(long id, CategoryRequestDTO categoryRequestDTO) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Category is not found with id: " + id));

        if ((existingCategory.getCategoryName()).equals(categoryRequestDTO.getCategoryName())  &&
        categoryRepository.findByCategoryName(categoryRequestDTO.getCategoryName()).isPresent()) {
            throw new IllegalArgumentException("Category with name " + categoryRequestDTO.getCategoryName() + " already exists");
        }
        existingCategory.setCategoryName(categoryRequestDTO.getCategoryName());
        return toCategoryResponseDTO(categoryRepository.save(existingCategory));
    }
    // ---

    @Override
    public void deleteCategory(long id) {

        if (!categoryRepository.existsById(id)) {
            throw new IllegalArgumentException("Category is not found with id: " + id);
        }

        // Check if any subcategories are associated with the category
        boolean hasSubCategories = subCategoryRepository.existsByCategoryId(id);
        if (hasSubCategories) {
            throw new IllegalStateException("Cannot delete Category with associated SubCategories.");
        }
        
        categoryRepository.deleteById(id);

        logger.info("Category with id {} was deleted.", id);
    }
    // ---

    @Override
    public List<CategoryWithSubCategoryResponseDTO> getAllCategoriesWithRelatedSubCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryWithSubCategoryResponseDTO> categoriesDTOs = categories.stream().map(this::toCategoryWithSubCategoryResponseDTO)
                .collect(Collectors.toList());
        return categoriesDTOs;
    }
}
