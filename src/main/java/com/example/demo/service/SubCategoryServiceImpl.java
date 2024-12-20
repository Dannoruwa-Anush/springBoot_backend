package com.example.demo.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.request.SubCategoryRequestDTO;
import com.example.demo.dto.response.SubCategoryResponseDTO;
import com.example.demo.dto.response.getById.SubCategoryGetByIdResponseDTO;
import com.example.demo.entity.Category;
import com.example.demo.entity.SubCategory;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.SubCategoryRepository;

@Service
public class SubCategoryServiceImpl implements SubCategoryService {

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryService categoryService; //to use DTO of category

    private static final Logger logger = LoggerFactory.getLogger(SubCategoryServiceImpl.class);

    // ****
    private SubCategoryResponseDTO toSubCategoryResponseDTO(SubCategory subCategory) {
        SubCategoryResponseDTO responseDto = new SubCategoryResponseDTO();
        responseDto.setId(subCategory.getId());
        responseDto.setSubCategoryName(subCategory.getSubCategoryName());

        // Include category name if the category is not null
        if (subCategory.getCategory() != null) {
            responseDto.setCategoryName(subCategory.getCategory().getCategoryName());
        }

        return responseDto;
    }
    //-----

    //-----
    private SubCategoryGetByIdResponseDTO toSubCategoryGetByIdResponseDTO(SubCategory subCategory){
        SubCategoryGetByIdResponseDTO dto = new SubCategoryGetByIdResponseDTO();
        dto.setId(subCategory.getId());
        dto.setSubCategoryName(subCategory.getSubCategoryName());
        dto.setCategory(categoryService.toCategoryResponseDTO(subCategory.getCategory()));
        return dto;
    }
    //-----
    // ****

    @Override
    public List<SubCategoryResponseDTO> getAllSubCategories() {
        List<SubCategory> subCategories = subCategoryRepository.findAll();
        List<SubCategoryResponseDTO> subcategoryDTOs = subCategories.stream().map(this::toSubCategoryResponseDTO)
                .collect(Collectors.toList());
        return subcategoryDTOs;
    }

    @Override
    public SubCategoryGetByIdResponseDTO getSubCategoryById(long id) {
        SubCategory subcategory = subCategoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("SubCategory is not found with id: " + id));

        return toSubCategoryGetByIdResponseDTO(subcategory);
    }
    // ---

    @Override
    public SubCategoryResponseDTO saveSubCategory(SubCategoryRequestDTO subCategorySaveRequestDTO) {
        Optional<SubCategory> subCategoryWithSameName = subCategoryRepository
                .findBySubCategoryName(subCategorySaveRequestDTO.getSubCategoryName());

        if (subCategoryWithSameName.isPresent()) {
            throw new IllegalArgumentException(
                    "SubCategory with name " + subCategorySaveRequestDTO.getSubCategoryName() + " already exists");
        }

        // Get related Category information of the SubCategory and set that to the
        // subCategory
        Category relatedCategory = categoryRepository.findById(subCategorySaveRequestDTO.getCategoryId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Category is not found with id: " + subCategorySaveRequestDTO.getCategoryId()));

        // create a new subCategory
        SubCategory subCategoryToSave = new SubCategory();
        subCategoryToSave.setSubCategoryName(subCategorySaveRequestDTO.getSubCategoryName());
        subCategoryToSave.setCategory(relatedCategory);

        return toSubCategoryResponseDTO(subCategoryRepository.save(subCategoryToSave));
    }
    // ---

    @Override
    public SubCategoryResponseDTO updateSubCategory(long id, SubCategoryRequestDTO subCategorySaveRequestDTO) {
        SubCategory existingSubCategory = subCategoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("SubCategory is not found with id: " + id));

        if ((existingSubCategory.getSubCategoryName()).equals(subCategorySaveRequestDTO.getSubCategoryName())) {
            throw new IllegalArgumentException(
                    "SubCategory with name " + subCategorySaveRequestDTO.getSubCategoryName() + " already exists");
        }

        // Get related Category information of the SubCategory and set that to the
        // subCategory
        Category relatedCategory = categoryRepository.findById(subCategorySaveRequestDTO.getCategoryId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Category is not found with id: " + subCategorySaveRequestDTO.getCategoryId()));

        // update existing subCategory
        existingSubCategory.setSubCategoryName(subCategorySaveRequestDTO.getSubCategoryName());
        existingSubCategory.setCategory(relatedCategory);

        return toSubCategoryResponseDTO(subCategoryRepository.save(existingSubCategory));
    }
    // ---

    @Override
    public void deleteSubCategory(long id) {

        if (!subCategoryRepository.existsById(id)) {
            throw new IllegalArgumentException("SubCategory is not found with id: " + id);
        }

        // Check if any books are associated with the subCategory
        boolean hasBooks = bookRepository.existsBySubCategoryId(id);
        if (hasBooks) {
            throw new IllegalStateException("Cannot delete SubCategory with associated Books.");
        }

        subCategoryRepository.deleteById(id);
        logger.info("SubCategory with id {} was deleted.", id);
    }
    // ---
}
