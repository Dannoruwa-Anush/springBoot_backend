package com.example.demo.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Category;
import com.example.demo.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // include Logger(slf4j) for auditing purposes
    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    // ---

    @Override
    public Category getCategoryById(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Category not found with id: " + id));
    }
    // ---

    @Override
    public Category saveCategory(Category category) {
        Optional<Category> CategoryWithSameName = categoryRepository.findByCategoryName(category.getCategoryName());

        if (CategoryWithSameName.isPresent()) {
            throw new IllegalArgumentException("Category with name " + category.getCategoryName() + " already exists");
        }
        return categoryRepository.save(category);
    }
    // ---

    @Override
    public Category updateCategory(long id, Category category) {
        Category existingCategory = getCategoryById(id);

        if ((existingCategory.getCategoryName()).equals(category.getCategoryName())) {
            throw new IllegalArgumentException("Category with name " + category.getCategoryName() + " already exists");
        }
        existingCategory.setCategoryName(category.getCategoryName());
        return categoryRepository.save(existingCategory);
    }
    // ---

    @Override
    public void deleteCategory(long id) {
        Optional<Category> existingCategory = categoryRepository.findById(id);

        if (!existingCategory.isPresent()) {
            throw new IllegalArgumentException("Category not found with id: " + id);
        }

        categoryRepository.deleteById(id);

        logger.info("Category with id {} was deleted.", id);
    }
    // ---
}
