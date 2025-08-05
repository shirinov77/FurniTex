package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long id;
    private User buyer;
    private Product product;
    private Integer quantity;
    private OrderStatus status;
    private LocalDateTime createdAt;
}
