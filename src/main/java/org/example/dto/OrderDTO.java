package org.example.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Buyurtma ma'lumotlarini o'tkazish uchun ishlatiladigan ob'ekt (DTO).
 * Buyurtmaning asosiy ma'lumotlarini, uning ichidagi mahsulotlar ro'yxatini,
 * umumiy miqdorini va holatini o'z ichiga oladi.
 */
@Data
public class OrderDTO {
    private Long id;
    private Long userId; // Foydalanuvchiga bog'langan
    private List<OrderItemDTO> items; // Buyurtmadagi mahsulotlar
    private Double totalAmount;
    private String status; // Masalan, "PENDING", "COMPLETED"
    private LocalDateTime orderDate;

    @Data
    public static class OrderItemDTO {
        private Long productId;
        private String productName;
        private Integer quantity;
        private Double price;
    }
}
