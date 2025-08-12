package com.acainfo.backend.subject.domain.repository;

import com.acainfo.backend.subject.domain.entity.Subject;

/**
 * Interfaz que define las operaciones de creación para la entidad Subject.
 *
 * Parte del patrón CRUD segregado siguiendo ISP (Interface Segregation Principle).
 * Las validaciones de unicidad se manejan a nivel de base de datos mediante
 * constraints, y las validaciones de formato en los DTOs de entrada.
 */
public interface CreateSubjectRepository {

    /**
     * Persiste una nueva asignatura en el sistema.
     *
     * @param subject la asignatura a crear
     * @return la asignatura creada con su ID generado
     */
    Subject save(Subject subject);
}