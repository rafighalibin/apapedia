package com.apapedia.catalogue.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.apapedia.catalogue.model.Category;

@Service
public interface CategoryService {
    Category createCategory(Category category);

    Category getCategoryById(UUID id);

    List<Category> getAllCategories();
}
