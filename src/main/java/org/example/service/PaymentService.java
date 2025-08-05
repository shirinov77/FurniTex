package org.example.service;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    /**
     * To'lov jarayonini simulyatsiya qiladi.
     * Kelajakda haqiqiy to'lov tizimi (masalan, Stripe yoki PayPal) bilan integratsiya qilish mumkin.
     * @param paymentMethod to'lov usuli
     * @param amount to'lov miqdori
     * @return to'lov muvaffaqiyatli bo'lsa `true`, aks holda `false`
     */
    public boolean processPayment(String paymentMethod, Double amount) {
        if ("cash".equalsIgnoreCase(paymentMethod)) {
            return amount > 0; // Oddiy tekshiruv
        }
        // Boshqa to'lov usullari uchun API integratsiyasi qo'shish mumkin
        return false;
    }
}