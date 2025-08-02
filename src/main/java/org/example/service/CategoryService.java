package org.example.service;

import jakarta.validation.Valid;
import org.example.model.Category;
import org.example.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Barcha kategoriyalarni olish.
     * @return barcha kategoriyalar ro'yxati
     */
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    /**
     * Yangi kategoriyani saqlash yoki mavjudini yangilash.
     * @param category saqlanadigan kategoriya obyekti
     */
    public void save(Category category) {
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Kategoriya nomi majburiy!");
        }
        categoryRepository.save(category);
    }

    /**
     * ID bo'yicha kategoriyani topish.
     * @param id kategoriya IDsi
     * @return kategoriya obyekti (Optional)
     */
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    /**
     * ID bo'yicha kategoriyani o'chirish.
     * @param id o'chiriladigan kategoriya IDsi
     */
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    /**
     * Kategoriyani yangilash.
     * @param id yangilanadigan kategoriya IDsi
     * @param category yangi ma'lumotlarni o'z ichiga olgan kategoriya obyekti
     * @return yangilangan kategoriya obyekti
     * @throws RuntimeException agar kategoriya topilmasa
     */
    public Category update(Long id, @Valid Category category) {
        // ID bo'yicha mavjud kategoriyani topish
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Yangilanadigan kategoriya topilmadi"));

        // Yangi ma'lumotlar bilan yangilash
        existingCategory.setName(category.getName());
        existingCategory.setDescription(category.getDescription());

        // Yangilangan kategoriyani saqlash va qaytarish
        return categoryRepository.save(existingCategory);
    }
}
