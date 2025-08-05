package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private String fullName;
    private String username;
    private String phone;
    private String password; // Shifrlangan bo'lishi kerak
    private UserRole role;   // Enum sifatida
}
