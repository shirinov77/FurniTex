package org.example.service;

import org.example.model.Cart;
import org.example.model.Order;
import org.example.model.OrderStatus;
import org.example.model.User;
import org.example.repository.CartRepository;
import org.example.repository.OrderRepository;
import org.example.repository.ProductRepository;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        UserRepository userRepository,
                        CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    /**
     * Buyurtmani saqlash.
     * @param order saqlanadigan buyurtma obyekti
     * @return saqlangan buyurtma obyekti
     */
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    /**
     * Barcha buyurtmalarni olish.
     * @return barcha buyurtmalar ro'yxati
     */
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    /**
     * ID bo'yicha buyurtmani topish.
     * @param id buyurtma IDsi
     * @return buyurtma obyekti (Optional)
     */
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    /**
     * ID bo'yicha buyurtmani o'chirish.
     * @param id o'chiriladigan buyurtma IDsi
     */
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    /**
     * Checkout: foydalanuvchi savatdagi barcha mahsulotlarni buyurtma qiladi.
     * @param username foydalanuvchi nomi
     */
    public void checkout(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Foydalanuvchi topilmadi: " + username));

        List<Cart> cartItems = cartRepository.findAllByUser(user);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Savat bo'sh, buyurtma berib bo'lmaydi.");
        }

        List<Order> orders = new ArrayList<>();
        for (Cart cartItem : cartItems) {
            Order order = new Order();
            order.setBuyer(user);
            order.setProduct(cartItem.getProduct());
            order.setQuantity(cartItem.getQuantity());
            order.setStatus(OrderStatus.PENDING);
            orders.add(order);
        }

        orderRepository.saveAll(orders);
        cartRepository.deleteAll(cartItems); // savatni tozalaymiz
    }

    /**
     * Foydalanuvchining barcha buyurtmalarini olish.
     * @param username foydalanuvchi nomi
     * @return foydalanuvchining buyurtmalari ro'yxati
     */
    public List<Order> getOrdersByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Foydalanuvchi topilmadi: " + username));
        return orderRepository.findByBuyerId(user.getId());
    }
}