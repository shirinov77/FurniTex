package org.example.service;

import jakarta.validation.Valid;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Barcha foydalanuvchilarni topish.
     * @return barcha foydalanuvchilar ro'yxati
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Yangi foydalanuvchini saqlash. Parolni shifrlaydi.
     * @param user saqlanadigan foydalanuvchi obyekti
     */
    public void save(User user) {
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
    }

    /**
     * Hozirgi tizimga kirgan foydalanuvchini olish.
     * @return hozirgi foydalanuvchi obyekti
     */
    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Joriy foydalanuvchi topilmadi"));
    }

    /**
     * Foydalanuvchi ma'lumotlarini yangilash.
     * @param user yangilanadigan foydalanuvchi obyekti
     */
    public void update(@Valid User user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("Foydalanuvchi IDsi bo'lmasa, yangilab bo'lmaydi.");
        }
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Yangilanadigan foydalanuvchi topilmadi"));

        existingUser.setFullName(user.getFullName());
        existingUser.setUsername(user.getUsername());
        existingUser.setPhone(user.getPhone());
        // Parol yangilanmaydi, agar yangi parol kiritilmasa.
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userRepository.save(existingUser);
    }

    /**
     * Foydalanuvchi nomiga (username) ko'ra foydalanuvchini topish.
     * @param username qidirilayotgan foydalanuvchi nomi
     * @return topilgan foydalanuvchi obyekti
     * @throws RuntimeException agar foydalanuvchi topilmasa
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Foydalanuvchi topilmadi: " + username));
    }
}
