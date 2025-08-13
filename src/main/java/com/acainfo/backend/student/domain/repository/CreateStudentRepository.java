package com.acainfo.backend.student.domain.repository;

import com.acainfo.backend.student.domain.entity.Student;

/**
 * Interfaz que define las operaciones de creación para la entidad Student.
 *
 * Parte del patrón CRUD segregado siguiendo ISP (Interface Segregation Principle).
 * Las validaciones de unicidad se manejan a nivel de base de datos mediante
 * constraints, y las validaciones de formato en los DTOs de entrada.
 */
public interface CreateStudentRepository {

    /**
     * Persiste un nuevo estudiante en el sistema.
     *
     * @param student el estudiante a crear
     * @return el estudiante creado con su ID generado
     */
    Student save(Student student);
}