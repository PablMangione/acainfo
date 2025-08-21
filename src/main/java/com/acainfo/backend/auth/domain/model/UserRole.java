package com.acainfo.backend.auth.domain.model;

/**
 * Roles disponibles en el sistema
 */
public enum UserRole {
    ROLE_STUDENT,      // Estudiante regular
    ROLE_TEACHER,      // Profesor sin permisos administrativos
    ROLE_ADMIN        // Profesor con permisos administrativos
}