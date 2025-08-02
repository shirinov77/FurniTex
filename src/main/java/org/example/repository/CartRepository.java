package org.example.repository;

import org.example.model.Cart;
import org.example.model.Product;
import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    /**
     * Foydalanuvchi IDsi bo'yicha savatni topish.
     * Optional qaytarish turi bilan savat topilmaganda null xatosini oldini oladi.
     *
     * @param userId savatni qidirish uchun foydalanuvchi IDsi
     * @return savat obyekti, agar topilsa, aks holda bo'sh Optional
     */
    Optional<Cart> findByUserId(Long userId);

    List<Cart> findAllByUser(User user);

    Optional<Cart> findByUserAndProduct(User user, Product product);

}
