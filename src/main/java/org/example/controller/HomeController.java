package org.example.controller;

import org.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    private final ProductService productService;

    @Autowired
    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping({"/", "/home"})
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("home");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        modelAndView.addObject("username", username != null && !username.equals("anonymousUser") ? username : "Mehmon");

        // Bazadan barcha mahsulotlarni olib, modelga qo'shish
        modelAndView.addObject("products", productService.findAll());

        return modelAndView;
    }

    @GetMapping("/user")
    public ModelAndView user() {
        ModelAndView modelAndView = new ModelAndView("user");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        modelAndView.addObject("username", username != null && !username.equals("anonymousUser") ? username : "Mehmon");
        return modelAndView;
    }
}