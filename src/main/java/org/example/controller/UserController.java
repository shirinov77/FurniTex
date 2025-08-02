package org.example.controller;

import jakarta.validation.Valid;
import org.example.model.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;



@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User()); // Yangi User ob'ekti yaratamiz
        return "user/register"; // templates/user/register.html
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("error", "Ro'yxatdan o'tishda xatolik: Ma'lumotlar to'g'ri kiritilmadi");
            return "user/register";
        }
        try {
            userService.save(user);
            return "redirect:/user/login?success=Ro'yxatdan o'tdingiz!";
        } catch (Exception e) {
            model.addAttribute("error", "Ro'yxatdan o'tishda xatolik: " + e.getMessage());
            return "user/register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "success", required = false) String success,
                                @RequestParam(value = "logout", required = false) String logout,
                                @RequestParam(value = "error", required = false) String error,
                                Model model) {
        if (success != null) {
            model.addAttribute("success", success);
        }
        if (logout != null) {
            model.addAttribute("logout", "Tizimdan muvaffaqiyatli chiqdingiz.");
        }
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "user/login"; // templates/user/login.html
    }

    @GetMapping("/profile")
    public String viewProfile(Model model) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            if (username == null || username.equals("anonymousUser")) {
                return "redirect:/user/login?error=Tizimga kiring!";
            }
            User currentUser = userService.findByUsername(username);
            model.addAttribute("user", currentUser);
            return "user/profile"; // templates/user/profile.html
        } catch (Exception e) {
            model.addAttribute("error", "Profilni yuklashda xatolik: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/profile/edit")
    public String editProfileForm(Model model) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            if (username == null || username.equals("anonymousUser")) {
                return "redirect:/user/login?error=Tizimga kiring!";
            }
            User currentUser = userService.findByUsername(username);
            model.addAttribute("user", currentUser);
            return "user/edit-profile";
        } catch (Exception e) {
            model.addAttribute("error", "Profilni tahrirlashda xatolik: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/profile/update")
    public String updateProfile(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("error", "Ma'lumotlar to'g'ri kiritilmadi");
            return "user/edit-profile";
        }
        try {
            userService.update(user);
            return "redirect:/user/profile?success=Profil yangilandi!";
        } catch (Exception e) {
            model.addAttribute("error", "Profilni yangilashda xatolik: " + e.getMessage());
            return "user/edit-profile";
        }
    }
}
