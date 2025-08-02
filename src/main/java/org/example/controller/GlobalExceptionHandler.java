package org.example.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleAllExceptions(Exception ex, Model model) {
        // Umumiy xatolarni tutish
        model.addAttribute("error", "Xatolik yuz berdi: " + ex.getMessage());
        return "error"; // error.html sahifasiga yo'naltirish
    }

    @ExceptionHandler(NullPointerException.class)
    public String handleNullPointerException(NullPointerException ex, Model model) {
        // Maxsus NullPointerException uchun
        model.addAttribute("error", "Ma'lumot topilmadi yoki noto'g'ri murojaat: " + ex.getMessage());
        return "error"; // error.html sahifasiga yo'naltirish
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException ex, Model model) {
        // Noto'g'ri argumentlar uchun
        model.addAttribute("error", "Noto'g'ri kiritilgan ma'lumot: " + ex.getMessage());
        return "error"; // error.html sahifasiga yo'naltirish
    }
}
