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
import com.example.demo.entity.Category;
import com.example.demo.entity.SubCategory;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.SubCategoryRepository;

@Service
public class SubCategoryServiceImpl implements SubCategoryService {

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

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
    // ****

    @Override
    public List<SubCategoryResponseDTO> getAllSubCategories() {
        List<SubCategory> subCategories = subCategoryRepository.findAll();
        List<SubCategoryResponseDTO> subcategoryDTOs = subCategories.stream().map(this::toSubCategoryResponseDTO)
                .collect(Collectors.toList());
        return subcategoryDTOs;
    }

    @Override
    public SubCategoryResponseDTO getSubCategoryById(long id) {
        SubCategory subcategory = subCategoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("SubCategory is not found with id: " + id));

        return toSubCategoryResponseDTO(subcategory);
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

        subCategoryRepository.deleteById(id);
        logger.info("SubCategory with id {} was deleted.", id);
    }
    // ---
}
