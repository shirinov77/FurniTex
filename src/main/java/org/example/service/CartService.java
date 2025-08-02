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

    /**
     * Savatni saqlash.
     * @param cart saqlanadigan savat obyekti
     * @return saqlangan savat obyekti
     */
    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }

    /**
     * ID bo'yicha savatni topish.
     * @param id savat IDsi
     * @return savat obyekti (Optional)
     */
    public Optional<Cart> findById(Long id) {
        return cartRepository.findById(id);
    }

    /**
     * Barcha savatdagi mahsulotlarni topish.
     * @return barcha savatdagi mahsulotlar ro'yxati
     */
    public List<Cart> findAll() {
        return cartRepository.findAll();
    }

    /**
     * ID bo'yicha savatni o'chirish.
     * @param id savat IDsi
     */
    public void deleteById(Long id) {
        cartRepository.deleteById(id);
    }

    /**
     * Foydalanuvchi IDsi bo'yicha savatdagi barcha mahsulotlarni olish.
     * @param userId foydalanuvchi IDsi
     * @return savatdagi mahsulotlar ro'yxati
     */
    public List<Cart> getCartItems(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Foydalanuvchi topilmadi"));
        return cartRepository.findAllByUser(user);
    }

    /**
     * Foydalanuvchi savatining umumiy narxini hisoblash.
     * @param userId foydalanuvchi IDsi
     * @return umumiy narx
     */
    public Double getTotal(Long userId) {
        return getCartItems(userId).stream()
                .mapToDouble(Cart::getTotalPrice)
                .sum();
    }

    /**
     * Mahsulotni foydalanuvchi savatiga qo'shish.
     * Agar mahsulot allaqachon savatda bo'lsa, miqdorini oshiradi.
     * @param userId foydalanuvchi IDsi
     * @param productId mahsulot IDsi
     */
    public void addProductToCart(Long userId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Mahsulot topilmadi"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Foydalanuvchi topilmadi"));

        Optional<Cart> existingCartItem = cartRepository.findByUserAndProduct(user, product);
        if (existingCartItem.isPresent()) {
            Cart cart = existingCartItem.get();
            cart.setQuantity(cart.getQuantity() + 1);
            cartRepository.save(cart);
        } else {
            Cart cart = new Cart();
            cart.setUser(user);
            cart.setProduct(product);
            cart.setQuantity(1);
            cartRepository.save(cart);
        }
    }

    /**
     * Mahsulotni savatdan o'chirish.
     * @param userId foydalanuvchi IDsi
     * @param productId mahsulot IDsi
     */
    public void removeProductFromCart(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Foydalanuvchi topilmadi"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Mahsulot topilmadi"));

        Optional<Cart> existingCartItem = cartRepository.findByUserAndProduct(user, product);
        existingCartItem.ifPresent(cartRepository::delete);
    }

    /**
     * Savatdagi mahsulot miqdorini yangilash.
     * @param userId foydalanuvchi IDsi
     * @param productId mahsulot IDsi
     * @param quantity yangi miqdor
     */
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
