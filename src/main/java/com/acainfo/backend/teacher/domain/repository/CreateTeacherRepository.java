package com.acainfo.backend.teacher.domain.repository;

import com.acainfo.backend.teacher.domain.entity.Teacher;

/**
 * Interfaz que define las operaciones de creación para la entidad Teacher.
 *
 * Parte del patrón CRUD segregado siguiendo ISP (Interface Segregation Principle).
 * Las validaciones de unicidad se manejan a nivel de base de datos mediante
 * constraints, y las validaciones de formato en los DTOs de entrada.
 */
public interface CreateTeacherRepository {

    /**
     * Persiste un nuevo profesor en el sistema.
     *
     * @param teacher el profesor a crear
     * @return el profesor creado con su ID generado
     */
    Teacher save(Teacher teacher);
}