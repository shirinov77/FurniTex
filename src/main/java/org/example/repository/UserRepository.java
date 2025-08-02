package org.example.repository;

import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Foydalanuvchi nomini (username) bo'yicha topish.
     * Optional qaytarish turi bilan foydalanuvchi topilmaganda xatolarni oldini oladi.
     *
     * @param username qidirish uchun foydalanuvchi nomi
     * @return foydalanuvchi obyekti, agar topilsa, aks holda bo'sh Optional
     */
    Optional<User> findByUsername(String username);

    /**
     * Elektron pochta manzili (email) bo'yicha topish.
     * Optional qaytarish turi bilan foydalanuvchi topilmaganda xatolarni oldini oladi.
     *
     * @param email qidirish uchun elektron pochta manzili
     * @return foydalanuvchi obyekti, agar topilsa, aks holda bo'sh Optional
     */
    Optional<User> findByEmail(String email);

    /**
     * Foydalanuvchi nomi mavjudligini tekshirish.
     *
     * @param username tekshirilayotgan foydalanuvchi nomi
     * @return agar mavjud bo'lsa `true`, aks holda `false`
     */
    boolean existsByUsername(String username);
}
