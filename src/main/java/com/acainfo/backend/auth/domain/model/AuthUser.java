package com.acainfo.backend.auth.domain.model;

import lombok.*;

import java.util.Set;

/**
 * Modelo de dominio para representar un usuario autenticado.
 * Unifica la información de Student y Teacher para el contexto de autenticación.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "password")
@EqualsAndHashCode(of = "id")
public class AuthUser {

    private String id; // Formato: "STUDENT_123" o "TEACHER_456"
    private String email;
    private String password;
    private String name;
    private UserType userType;
    private Set<UserRole> roles;
    private boolean isActive;

    /**
     * Obtiene el ID numérico sin el prefijo del tipo
     */
    public Long getNumericId() {
        if (id == null) return null;
        String[] parts = id.split("_");
        return parts.length > 1 ? Long.parseLong(parts[1]) : null;
    }

    /**
     * Crea un ID compuesto con el tipo y el ID numérico
     */
    public static String createId(UserType type, Long numericId) {
        return type.name() + "_" + numericId;
    }
}