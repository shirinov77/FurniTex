package org.example.dto;

import lombok.Data;
import java.util.List;

/**
 * Savat ma'lumotlarini o'tkazish uchun ishlatiladigan ob'ekt (DTO).
 * Bu klass savatning asosiy ma'lumotlarini, uning ichidagi mahsulotlar ro'yxatini
 * va umumiy narxini o'z ichiga oladi.
 */
@Data
public class CartDTO {
    private Long id;
    private Long userId; // Foydalanuvchiga bog'langan
    private List<CartItemDTO> items; // Savatdagi mahsulotlar ro'yxati
    private Double totalPrice;

    @Data
    public static class CartItemDTO {
        private Long productId;
        private String productName;
        private Integer quantity;
        private Double price;
    }
}
