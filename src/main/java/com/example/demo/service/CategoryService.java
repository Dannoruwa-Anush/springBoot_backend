package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Category;

public interface CategoryService {

    List<Category> getAllCategories();

    Category getCategoryById(long id);

    Category saveCategory(Category category);

    Category updateCategory(long id, Category category);

    void deleteCategory(long id);
}
