package org.example.repository;

import org.example.model.Order;
import org.example.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    /**
     * Foydalanuvchi IDsi bo'yicha barcha buyurtmalarni topish.
     *
     * @param userId buyurtmalarni qidirish uchun foydalanuvchi IDsi
     * @return foydalanuvchining buyurtmalari ro'yxati
     */
    List<Order> findByBuyerId(Long userId);

    /**
     * Status bo'yicha barcha buyurtmalarni topish.
     *
     * @param status buyurtma holati
     * @return berilgan statusga mos keluvchi buyurtmalar ro'yxati
     */
    List<Order> findByStatus(OrderStatus status);

    /**
     * Foydalanuvchi IDsi va status bo'yicha buyurtmalarni topish.
     *
     * @param userId buyurtmalarni qidirish uchun foydalanuvchi IDsi
     * @param status buyurtma holati
     * @return berilgan foydalanuvchi va statusga mos keluvchi buyurtmalar ro'yxati
     */
    List<Order> findByBuyerIdAndStatus(Long userId, OrderStatus status);
}
