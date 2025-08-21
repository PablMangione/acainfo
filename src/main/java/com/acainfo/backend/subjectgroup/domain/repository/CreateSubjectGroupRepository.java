package com.acainfo.backend.subjectgroup.domain.repository;

import com.acainfo.backend.subjectgroup.domain.entity.SubjectGroup;

/**
 * Interfaz que define las operaciones de creación para la entidad SubjectGroup.
 *
 * Parte del patrón CRUD segregado siguiendo ISP (Interface Segregation Principle).
 * Las validaciones de unicidad se manejan a nivel de base de datos mediante
 * constraints, y las validaciones de formato en los DTOs de entrada.
 */
public interface CreateSubjectGroupRepository {

    /**
     * Persiste un nuevo grupo de asignatura en el sistema.
     *
     * @param subjectGroup el grupo a crear
     * @return el grupo creado con su ID generado
     */
    SubjectGroup save(SubjectGroup subjectGroup);
}