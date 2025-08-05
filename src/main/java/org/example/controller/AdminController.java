package org.example.controller;

import org.example.model.Category;
import org.example.model.Product;
import org.example.service.CategoryService;
import org.example.service.ProductService;
import org.example.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final UserService userService;

    public AdminController(CategoryService categoryService, ProductService productService, UserService userService){
        this.categoryService = categoryService;
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping
    public String adminHome() {
        return "admin/home";
    }

    // ======= CATEGORY =======

    @GetMapping("/categories")
    public String listCategories(Model model) {
        return handleWithModel(model, () -> {
            model.addAttribute("categories", categoryService.findAll());
            return "admin/categories";
        });
    }

    @GetMapping("/categories/add")
    public String addCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "admin/add-category";
    }

    @PostMapping("/categories/add")
    public String addCategory(@ModelAttribute("category") Category category, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/add-category";
        }
        categoryService.save(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/categories/edit/{id}")
    public String editCategoryForm(@PathVariable Long id, Model model) {
        Optional<Category> category = categoryService.findById(id);
        if (category.isEmpty()) {
            return "redirect:/admin/categories";
        }
        model.addAttribute("category", category.get());
        return "admin/edit-category";
    }

    @PostMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable Long id, @ModelAttribute("category") Category category, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/edit-category";
        }
        categoryService.update(id, category);
        return "redirect:/admin/categories";
    }

    @PostMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return "redirect:/admin/categories";
    }

    // ======= PRODUCTS =======

    @GetMapping("/products")
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin/products";
    }

    @GetMapping("/products/add")
    public String addProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("users", userService.findAll());
        return "admin/add-product";
    }

    @PostMapping("/products/add")
    public String addProduct(@ModelAttribute("product") Product product, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("users", userService.findAll());
            return "admin/add-product";
        }
        productService.saveProduct(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/products/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("users", userService.findAll());
        return "admin/edit-product";
    }

    @PostMapping("/products/edit/{id}")
    public String editProduct(@PathVariable Long id, @ModelAttribute("product") Product product, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("users", userService.findAll());
            return "admin/edit-product";
        }
        product.setId(id);
        productService.updateProduct(id, product);  // <-- E'tibor bering: id qo‘shildi
        return "redirect:/admin/products";
    }

    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }

    // ======= USERS =======

    @GetMapping("/users")
    public String listUsers(Model model) {
        return handleWithModel(model, () -> {
            model.addAttribute("users", userService.findAll());
            return "admin/users";
        });
    }

    // ======= HELPERS =======

    private String handleWithModel(Model model, ControllerAction action) {
        try {
            return action.execute();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @FunctionalInterface
    interface ControllerAction {
        String execute() throws Exception;
    }
}
