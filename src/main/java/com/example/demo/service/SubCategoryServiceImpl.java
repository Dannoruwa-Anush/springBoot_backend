package com.example.demo.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.request.SubCategoryRequestDTO;
import com.example.demo.entity.Category;
import com.example.demo.entity.SubCategory;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.SubCategoryRepository;

@Service
public class SubCategoryServiceImpl implements SubCategoryService{
    
    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private static final Logger logger =LoggerFactory.getLogger(SubCategoryServiceImpl.class);

    @Override
    public List<SubCategory> getAllSubCategories() {
        return subCategoryRepository.findAll();
    }

    @Override
    public SubCategory getSubCategoryById(long id) {
        return subCategoryRepository.findById(id).orElseThrow(() -> new NoSuchElementException("SubCategory is not found with id: " + id));
    }
    //---

    @Override
    public SubCategory saveSubCategory(SubCategoryRequestDTO subCategorySaveRequestDTO) {
        Optional<SubCategory> subCategoryWithSameName = subCategoryRepository.findBySubCategoryName(subCategorySaveRequestDTO.getSubCategoryName());
        
        if(subCategoryWithSameName.isPresent()){
            throw new IllegalArgumentException("SubCategory with name " + subCategorySaveRequestDTO.getSubCategoryName() + " already exists");
        }

        //Get related Category information of the SubCategory and set that to the subCategory
        Category relatedCategory = categoryRepository.findById(subCategorySaveRequestDTO.getCategoryId()).orElseThrow(() -> new NoSuchElementException("Category is not found with id: " + subCategorySaveRequestDTO.getCategoryId()));        
        
        //create a new subCategory
        SubCategory subCategory = new SubCategory(); 
        subCategory.setSubCategoryName(subCategorySaveRequestDTO.getSubCategoryName());
        subCategory.setCategory(relatedCategory);

        return subCategoryRepository.save(subCategory);
    }
    //---

    @Override
    public SubCategory updateSubCategory(long id, SubCategoryRequestDTO subCategorySaveRequestDTO) {
        SubCategory existingSubCategory = getSubCategoryById(id);
        
        if((existingSubCategory.getSubCategoryName()).equals(subCategorySaveRequestDTO.getSubCategoryName())){
            throw new IllegalArgumentException("SubCategory with name " + subCategorySaveRequestDTO.getSubCategoryName() + " already exists");
        }

        //Get related Category information of the SubCategory and set that to the subCategory
        Category relatedCategory = categoryRepository.findById(subCategorySaveRequestDTO.getCategoryId()).orElseThrow(() -> new NoSuchElementException("Category is not found with id: " + subCategorySaveRequestDTO.getCategoryId()));        

        //update existing subCategory
        existingSubCategory.setSubCategoryName(subCategorySaveRequestDTO.getSubCategoryName()); 
        existingSubCategory.setCategory(relatedCategory); 

        return subCategoryRepository.save(existingSubCategory);
    }
    //---

    @Override
    public void deleteSubCategory(long id) {
        Optional<SubCategory> existingSubCategory = subCategoryRepository.findById(id);

        if(!existingSubCategory.isPresent()){
            throw new IllegalArgumentException("SubCategory is not found with id: " + id);
        }

        subCategoryRepository.deleteById(id);
        logger.info("SubCategory with id {} was deleted.", id);
    }
    //---
}
