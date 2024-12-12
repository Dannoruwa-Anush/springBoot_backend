package com.example.demo.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.entity.SubCategory;
import com.example.demo.repository.SubCategoryRepository;

@Service
public class SubCategoryServiceImpl implements SubCategoryService{
    
    @AutoWired
    private SubCategoryRepository subCategoryRepository;

    private static final Logger logger =LoggerFactory.getLogger(SubCategoryServiceImpl.class);

    @Override
    public List<SubCategory> getAllSubCategories() {
        return subCategoryRepository.findAll();
    }

    @Override
    public SubCategory getSubCategoryById(long id) {
        return subCategoryRepository.findById(id).orElseThrow(() -> new NoSuchElementException("SubCategory not found with id: " + id));
    }
    //---

    @Override
    public SubCategory saveSubCategory(SubCategory subCategory) {
        Optional<SubCategory> subCategoryWithSameName = subCategoryRepository.findBySubCategoryName(subCategory.getSubCategoryName());
        
        if(subCategoryWithSameName.isPresent()){
            throw new IllegalArgumentException("SubCategory with name " + subCategory.getSubCategoryName() + " already exists");
        }

        return subCategoryRepository.save(subCategory);
    }
    //---

    @Override
    public SubCategory updateSubCategory(long id, SubCategory subCategory) {
        SubCategory existingSubCategory = getSubCategoryById(id);
        
        if((existingSubCategory.getSubCategoryName()).equals(subCategory.getSubCategoryName())){
            throw new IllegalArgumentException("SubCategory with name " + subCategory.getSubCategoryName() + " already exists");
        }

        existingSubCategory.setSubCategoryName(subCategory.getSubCategoryName());  
        return subCategoryRepository.save(subCategory);
    }
    //---

    @Override
    public void deleteSubCategory(long id) {
        Optional<SubCategory> existingSubCategory = subCategoryRepository.findById(id);

        if(!existingSubCategory.isPresent()){
            throw new IllegalArgumentException("SubCategory not found with id: " + id);
        }

        subCategoryRepository.deleteById(id);
        logger.info("SubCategory with id {} was deleted.", id);
    }
    //---
}
