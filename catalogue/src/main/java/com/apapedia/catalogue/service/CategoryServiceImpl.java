package com.apapedia.catalogue.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apapedia.catalogue.model.Category;
import com.apapedia.catalogue.repository.CategoryDb;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDb categoryDb;

    @Override
    public Category createCategory(Category category) {
        return categoryDb.save(category);
    }

    @Override
    public Category getCategoryById(UUID id) {
        return categoryDb.findAllById(id);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryDb.findAll();
    }

}
