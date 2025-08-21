package com.acainfo.backend.enrollment.domain.repository;

import com.acainfo.backend.enrollment.domain.entity.Enrollment;

/**
 * Interfaz que define las operaciones de creación para la entidad Enrollment.
 *
 * Parte del patrón CRUD segregado siguiendo ISP (Interface Segregation Principle).
 * Las validaciones de unicidad se manejan a nivel de base de datos mediante
 * constraints, y las validaciones de formato en los DTOs de entrada.
 */
public interface CreateEnrollmentRepository {

    /**
     * Persiste una nueva inscripción en el sistema.
     *
     * @param enrollment la inscripción a crear
     * @return la inscripción creada con su ID generado
     */
    Enrollment save(Enrollment enrollment);
}