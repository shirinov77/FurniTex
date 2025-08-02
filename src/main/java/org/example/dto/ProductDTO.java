package org.example.dto;

import lombok.Data;

/**
 * Mahsulot ma'lumotlarini o'tkazish uchun ishlatiladigan ob'ekt (DTO).
 * Mahsulotning nomi, tavsifi, narxi, kategoriyasi va ombor miqdorini o'z ichiga oladi.
 */
@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Long categoryId; // Kategoriyaga bog'lanish
    private Integer stock; // Ombor miqdori
}
