package org.example.controller;

import org.example.model.User;
import org.example.service.CartService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public String viewCart(Model model, Authentication authentication) {
        Long userId = ((User) authentication.getPrincipal()).getId();
        model.addAttribute("cartItems", cartService.getCartItems(userId));
        model.addAttribute("total", cartService.getTotal(userId));
        return "cart/view"; // templates/cart/view.html
    }

    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId, Authentication authentication, Model model) {
        try {
            Long userId = ((User) authentication.getPrincipal()).getId();
            cartService.addProductToCart(userId, productId);
            return "redirect:/cart";
        } catch (Exception e) {
            model.addAttribute("error", "Mahsulotni savatga qo‘shishda xatolik: " + e.getMessage());
            return "redirect:/products"; // yoki kerakli sahifaga
        }
    }

    @PostMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId, Authentication authentication, Model model) {
        try {
            Long userId = ((User) authentication.getPrincipal()).getId();
            cartService.removeProductFromCart(userId, productId);
            return "redirect:/cart";
        } catch (Exception e) {
            model.addAttribute("error", "Mahsulotni savatdan o‘chirishda xatolik: " + e.getMessage());
            return "cart/view";
        }
    }

    @PostMapping("/update/{productId}")
    public String updateCartItem(@PathVariable Long productId,
                                 @RequestParam int quantity,
                                 Authentication authentication,
                                 Model model) {
        try {
            Long userId = ((User) authentication.getPrincipal()).getId();
            cartService.updateCartItem(userId, productId, quantity);
            return "redirect:/cart";
        } catch (Exception e) {
            model.addAttribute("error", "Savatni yangilashda xatolik: " + e.getMessage());
            return "cart/view";
        }
    }
}