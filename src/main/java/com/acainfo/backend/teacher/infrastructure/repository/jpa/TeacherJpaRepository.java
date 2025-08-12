package com.acainfo.backend.teacher.infrastructure.repository.jpa;

import com.acainfo.backend.teacher.infrastructure.repository.jpa.entity.TeacherJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherJpaRepository extends JpaRepository<TeacherJpa, Long> {

    /**
     * Busca un profesor por su email
     */
    Optional<TeacherJpa> findByEmail(String email);

    /**
     * Verifica si existe un profesor con el email dado
     */
    boolean existsByEmail(String email);

    /**
     * Obtiene todos los profesores administradores
     */
    List<TeacherJpa> findByIsAdminTrue();

    /**
     * Obtiene todos los profesores no administradores
     */
    List<TeacherJpa> findByIsAdminFalse();

    /**
     * Búsqueda por texto en el nombre (case-insensitive)
     */
    List<TeacherJpa> findByNameContainingIgnoreCase(String name);
}