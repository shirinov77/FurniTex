package org.example.dto;

import lombok.Data;

/**
 * Kategoriya ma'lumotlarini o'tkazish uchun ishlatiladigan ob'ekt (DTO).
 * Kategoriya nomi va tavsifini o'z ichiga oladi.
 */
@Data
public class CategoryDTO {
    private Long id;
    private String name;
    private String description;
}
