package org.example.service;

import jakarta.validation.Valid;
import org.example.model.Product;
import org.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Mahsulotni saqlash.
     * @param product saqlanadigan mahsulot obyekti
     * @return saqlangan mahsulot obyekti
     */
    public Product save(Product product) {
        return productRepository.save(product);
    }

    /**
     * Barcha mahsulotlarni topish.
     * @return barcha mahsulotlar ro'yxati
     */
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    /**
     * ID bo'yicha mahsulotni topish.
     * @param id mahsulot IDsi
     * @return mahsulot obyekti (Optional)
     */
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    /**
     * ID bo'yicha mahsulotni o'chirish.
     * @param id o'chiriladigan mahsulot IDsi
     */
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    /**
     * Barcha mahsulotlarni olish.
     * @return mahsulotlar ro'yxati
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * ID bo'yicha mahsulotni olish.
     * @param id mahsulot IDsi
     * @return mahsulot obyekti
     * @throws RuntimeException agar mahsulot topilmasa
     */
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mahsulot topilmadi"));
    }

    /**
     * Mahsulotni o'chirish.
     * @param id o'chiriladigan mahsulot IDsi
     */
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    /**
     * Mahsulotni yangilash.
     * @param id yangilanadigan mahsulot IDsi
     * @param product yangi ma'lumotlarni o'z ichiga olgan mahsulot obyekti
     * @return yangilangan mahsulot obyekti
     * @throws RuntimeException agar mahsulot topilmasa
     */
    public Product update(Long id, @Valid Product product) {
        // ID bo'yicha mavjud mahsulotni topish
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Yangilanadigan mahsulot topilmadi"));

        // Yangi ma'lumotlar bilan yangilash
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setImagePath(product.getImagePath());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setSeller(product.getSeller());

        // Yangilangan mahsulotni saqlash va qaytarish
        return productRepository.save(existingProduct);
    }
}