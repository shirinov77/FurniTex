package org.example.service;

import org.example.model.Cart;
import org.example.model.Product;
import org.example.model.User;
import org.example.repository.CartRepository;
import org.example.repository.ProductRepository;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public CartService(CartRepository cartRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }

    public Optional<Cart> findById(Long id) {
        return cartRepository.findById(id);
    }

    public List<Cart> findAll() {
        return cartRepository.findAll();
    }

    public List<Cart> getCartItems(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Foydalanuvchi topilmadi"));
        return cartRepository.findAllByUser(user);
    }

    public Double getTotal(Long userId) {
        List<Cart> cartItems = getCartItems(userId);
        return cartItems.stream()
                .mapToDouble(cartItem -> cartItem.getProductPrice() * cartItem.getQuantity())
                .sum();
    }

    public void addProductToCart(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Foydalanuvchi topilmadi"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Mahsulot topilmadi"));

        Optional<Cart> existingCartItem = cartRepository.findByUserAndProduct(user, product);
        if (existingCartItem.isPresent()) {
            Cart cart = existingCartItem.get();
            cart.setQuantity(cart.getQuantity() + 1);
            cartRepository.save(cart);
        } else {
            Cart newCartItem = new Cart();
            newCartItem.setUser(user);
            newCartItem.setProduct(product);
            newCartItem.setQuantity(1);
            cartRepository.save(newCartItem);
        }
    }

    public void removeProductFromCart(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Foydalanuvchi topilmadi"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Mahsulot topilmadi"));

        Optional<Cart> existingCartItem = cartRepository.findByUserAndProduct(user, product);
        existingCartItem.ifPresent(cartRepository::delete);
    }

    public void updateCartItem(Long userId, Long productId, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Foydalanuvchi topilmadi"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Mahsulot topilmadi"));

        Optional<Cart> existingCartItem = cartRepository.findByUserAndProduct(user, product);
        if (existingCartItem.isPresent()) {
            Cart cart = existingCartItem.get();
            if (quantity > 0) {
                cart.setQuantity(quantity);
                cartRepository.save(cart);
            } else {
                cartRepository.delete(cart);
            }
        }
    }
}
