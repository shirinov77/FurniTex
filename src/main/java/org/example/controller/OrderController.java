package org.example.controller;

import org.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/checkout")
    public String checkoutOrder(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            model.addAttribute("error", "Buyurtma berish uchun tizimga kiring!");
            return "user/login"; // login sahifangiz templates/auth/login.html bo'lishi kerak
        }

        try {
            String username = auth.getName();
            orderService.checkout(username);
            return "redirect:/order/success";
        } catch (Exception e) {
            model.addAttribute("error", "Buyurtma jarayonida xatolik yuz berdi: " + e.getMessage());
            return "cart/view"; // templates/cart/view.html bo'lishi kerak
        }
    }

    @GetMapping("/success")
    public String orderSuccessPage(Model model) {
        model.addAttribute("message", "Buyurtmangiz muvaffaqiyatli qabul qilindi!");
        return "order/success"; // templates/order/success.html kerak bo'ladi
    }

    @GetMapping("/list")
    public String listOrders(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return "redirect:/user/login"; // auth/login o'rniga user/login
        }

        String username = auth.getName();
        model.addAttribute("orders", orderService.getOrdersByUsername(username));
        return "order/list"; // templates/order/list.html bo'lishi kerak
    }
}