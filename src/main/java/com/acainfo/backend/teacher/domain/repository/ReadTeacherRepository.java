package com.acainfo.backend.teacher.domain.repository;

import com.acainfo.backend.teacher.domain.entity.Teacher;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones de lectura para la entidad Teacher.
 *
 * Contiene operaciones de consulta sin efectos secundarios.
 * Los filtrados complejos y validaciones se manejan en la capa de servicio.
 */
public interface ReadTeacherRepository {

    /**
     * Busca un profesor por su ID.
     */
    Optional<Teacher> findById(Long id);

    /**
     * Obtiene todos los profesores.
     */
    List<Teacher> findAll();

    /**
     * Verifica si existe un profesor con el ID dado.
     */
    boolean existsById(Long id);

    /**
     * Busca un profesor por su email.
     * Útil para autenticación y validación de unicidad.
     */
    Optional<Teacher> findByEmail(String email);

    /**
     * Verifica si existe un profesor con el email dado.
     */
    boolean existsByEmail(String email);

    /**
     * Obtiene todos los profesores administradores.
     */
    List<Teacher> findByIsAdminTrue();

    /**
     * Obtiene todos los profesores no administradores.
     */
    List<Teacher> findByIsAdminFalse();

    /**
     * Búsqueda por texto en el nombre (case-insensitive).
     */
    List<Teacher> findByNameContainingIgnoreCase(String name);
}