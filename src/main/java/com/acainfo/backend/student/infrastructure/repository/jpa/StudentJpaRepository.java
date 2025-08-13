package com.acainfo.backend.student.infrastructure.repository.jpa;

import com.acainfo.backend.globalenum.Major;
import com.acainfo.backend.student.infrastructure.repository.jpa.entity.StudentJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentJpaRepository extends JpaRepository<StudentJpa, Long> {

    /**
     * Busca un estudiante por su email
     */
    Optional<StudentJpa> findByEmail(String email);

    /**
     * Verifica si existe un estudiante con el email dado
     */
    boolean existsByEmail(String email);

    /**
     * Busca estudiantes por carrera
     */
    List<StudentJpa> findByMajor(Major major);

    /**
     * Busca estudiantes activos
     */
    List<StudentJpa> findByIsActiveTrue();

    /**
     * Busca estudiantes inactivos
     */
    List<StudentJpa> findByIsActiveFalse();

    /**
     * Busca estudiantes activos por carrera
     */
    List<StudentJpa> findByMajorAndIsActiveTrue(Major major);

    /**
     * Búsqueda por texto en el nombre (case-insensitive)
     */
    List<StudentJpa> findByNameContainingIgnoreCase(String name);

    /**
     * Búsqueda por texto en el apellido (case-insensitive)
     */
    List<StudentJpa> findByLastNameContainingIgnoreCase(String lastName);

    /**
     * Búsqueda por texto en nombre o apellido (case-insensitive)
     */
    List<StudentJpa> findByNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String name, String lastName);

    /**
     * Cuenta estudiantes por carrera
     */
    long countByMajor(Major major);

    /**
     * Cuenta estudiantes activos
     */
    long countByIsActiveTrue();
}