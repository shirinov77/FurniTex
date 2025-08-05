package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    private Long id;

    // Foydalanuvchi va mahsulot ID'lari
    private Long productId;
    private Long userId;

    private int quantity;

    // Mahsulot narxi hisoblash uchun (DBga yozilmaydi)
    private Double productPrice;

    // Foydalanuvchi va mahsulot obyektlari (optional)
    private Product product;
    private User user;

    public Double getTotalPrice() {
        return productPrice != null ? productPrice * quantity : 0.0;
    }

    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            this.userId = user.getId();
        }
    }

    public void setProduct(Product product) {
        this.product = product;
        if (product != null) {
            this.productId = product.getId();
        }
    }
}
