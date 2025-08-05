package org.example.service;

import org.example.model.Cart;
import org.example.model.Order;
import org.example.model.OrderStatus;
import org.example.model.User;
import org.example.repository.CartRepository;
import org.example.repository.OrderRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    public OrderService(OrderRepository orderRepository,
                        UserRepository userRepository,
                        CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    public Order save(Order order) {
        order.setCreatedAt(LocalDateTime.now());
        orderRepository.save(order);
        return order;
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    public void checkout(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Foydalanuvchi topilmadi: " + username));

        List<Cart> cartItems = cartRepository.findAllByUser(user);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Savat bo'sh!");
        }

        List<Order> orders = new ArrayList<>();
        for (Cart cartItem : cartItems) {
            Order order = new Order();
            order.setBuyer(user);
            order.setProduct(cartItem.getProduct());
            order.setQuantity(cartItem.getQuantity());
            order.setStatus(OrderStatus.PENDING);
            order.setCreatedAt(LocalDateTime.now());
            orders.add(order);
        }

        orderRepository.saveAll(orders);
        cartRepository.deleteAll(cartItems);
    }

    public List<Order> getOrdersByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Foydalanuvchi topilmadi: " + username));
        return orderRepository.findByBuyerId(user.getId());
    }
}
