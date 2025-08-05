package org.example.controller;

import org.example.model.Product;
import org.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Barcha mahsulotlar ro'yxatini ko'rsatadi.
     * @param model Mahsulotlar ro'yxatini HTML shabloniga uzatish uchun
     * @return products/list.html shablonini qaytaradi
     */
    @GetMapping
    public String getAllProducts(Model model) {
        try {
            model.addAttribute("products", productService.getAllProducts());
            // Agar rasm yo'li null bo'lsa, xato berishini oldini olish uchun
            // har bir mahsulotni tekshirish mumkin, lekin bu frontendda ham hal etilishi mumkin.
            return "product/list"; // templates/product/list.html
        } catch (Exception e) {
            // Xato yuz berganda xato sahifasiga yo'naltirish
            model.addAttribute("error", "Mahsulotlarni yuklashda xatolik: " + e.getMessage());
            return "error";
        }
    }

    /**
     * Berilgan ID bo'yicha mahsulot haqida batafsil ma'lumotlarni ko'rsatadi.
     * @param id Mahsulotning unikal identifikatori
     * @param model Mahsulot ob'ektini HTML shabloniga uzatish uchun
     * @return products/details.html shablonini qaytaradi
     */
    @GetMapping("/{id}")
    public String getProductDetails(@PathVariable Long id, Model model) {
        try {
            // Service qatlami orqali mahsulotni ID bo'yicha qidirish
            Product product = productService.getProductById(id);
            if (product == null) {
                model.addAttribute("error", "Mahsulot topilmadi.");
                return "error";
            }
            model.addAttribute("product", product);
            return "product/details"; // templates/product/details.html
        } catch (Exception e) {
            model.addAttribute("error", "Xatolik yuz berdi: " + e.getMessage());
            return "error";
        }
    }
}