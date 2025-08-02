package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Savatdagi har bir mahsulotni ifodalovchi entity.
@Entity
@Table(name = "carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ushbu savatdagi mahsulot.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Ushbu savatdagi foydalanuvchi.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Mahsulot miqdori.
    @Column(nullable = false)
    private int quantity;

    // Ma'lumotlar bazasida saqlanmaydi, faqat hisoblash uchun.
    @Transient
    public Double getTotalPrice() {
        return product != null && product.getPrice() != null ? product.getPrice() * quantity : 0.0;
    }
}
