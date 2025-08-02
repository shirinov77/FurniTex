package org.example.dto;

import lombok.Data;

/**
 * Foydalanuvchi ma'lumotlarini o'tkazish uchun ishlatiladigan ob'ekt (DTO).
 * Bu klass foydalanuvchining ma'lumotlarini o'z ichiga oladi, lekin
 * parolni faqat ma'lumotlar bazasiga saqlash uchun ishlatish kerak,
 * DTO'dan keyingi ishlarda emas.
 */
@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String password; // Faqat DTO uchun, shifrlashdan keyin ishlatilmasligi kerak
    private String role; // Masalan, "USER" yoki "ADMIN"
    private String fullName;
}
