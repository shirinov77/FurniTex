package org.example.repository;

import org.example.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    /**
     * Kategoriya IDsi bo'yicha mahsulotlarni topish.
     *
     * @param categoryId mahsulotlarni qidirish uchun kategoriya IDsi
     * @return berilgan kategoriyaga tegishli mahsulotlar ro'yxati
     */
    List<Product> findByCategoryId(Long categoryId);

    /**
     * Narxlar oralig'i bo'yicha mahsulotlarni filtrlash.
     *
     * @param minPrice minimal narx
     * @param maxPrice maksimal narx
     * @return narxlar oralig'iga mos keluvchi mahsulotlar ro'yxati
     */
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    /**
     * Mahsulot nomi bo'yicha qidiruv (katta-kichik harflarni e'tiborsiz qoldirish).
     *
     * @param name qidirilayotgan mahsulot nomi
     * @return nomga mos keluvchi mahsulotlar ro'yxati
     */
    List<Product> findByNameContainingIgnoreCase(String name);
}
