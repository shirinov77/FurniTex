package org.example.service;

import jakarta.validation.Valid;
import org.example.model.Category;
import org.example.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public void save(Category category) {
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Kategoriya nomi majburiy!");
        }
        categoryRepository.save(category);
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    public Category update(Long id, @Valid Category category) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Yangilanadigan kategoriya topilmadi"));

        existingCategory.setName(category.getName());
        existingCategory.setDescription(category.getDescription());

        categoryRepository.update(existingCategory);

        return existingCategory;
    }
}
