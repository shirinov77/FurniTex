package org.example.config;

import org.example.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.AuthenticationManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    // CustomUserDetailsService'ni konstruktor orqali injection qilamiz.
    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Saytdagi URL'larga kirish huquqlarini sozlaymiz.
                .authorizeHttpRequests(auth -> auth
                        // Faqat ADMIN roli bo'lgan foydalanuvchilar "/admin/**" URL'lariga kirishi mumkin.
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // Faqat tizimga kirgan (authenticated) foydalanuvchilar "/user/**" URL'lariga kirishi mumkin.
                        .requestMatchers("/user/**", "/cart/**", "/order/**").authenticated()
                        // Boshqa barcha URL'larga hamma ruxsat etilgan.
                        .anyRequest().permitAll()
                )
                // Formali kirishni sozlaymiz.
                .formLogin(form -> form
                        // Kirish sahifasining URL'i.
                        .loginPage("/user/login")
                        // Kirish muvaffaqiyatli bo'lgandan so'ng yo'naltiriladigan sahifa.
                        .defaultSuccessUrl("/home", true)
                        // Kirishga ruxsat berish.
                        .permitAll()
                )
                // Tizimdan chiqishni sozlaymiz.
                .logout(logout -> logout
                        // Tizimdan chiqish URL'i.
                        .logoutUrl("/logout")
                        // Chiqishdan so'ng foydalanuvchini yo'naltiruvchi sahifa.
                        .logoutSuccessUrl("/user/login?logout")
                        // Tizimdan chiqishga hamma ruxsat etilgan.
                        .permitAll()
                )
                // Xatolarni boshqarishni sozlaymiz.
                .exceptionHandling(exception -> exception
                        // Kirish huquqi bo'lmaganida yo'naltiriladigan sahifa.
                        .accessDeniedPage("/access-denied")
                )
                // !!! Diqqat: CSRF himoyasini o'chirish tavsiya etilmaydi!
                // Loyihani productionga chiqarishdan oldin uni yoqish (disable()) yoki
                // to'g'ri ishlatish kerak.
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    // Parollarni shifrlash uchun PasswordEncoder'ni sozlaymiz. BCrypt tavsiya etiladi.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Foydalanuvchi ma'lumotlarini qidirish uchun DaoAuthenticationProvider'ni sozlaymiz.
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
}