package com.acainfo.backend.student.domain.repository;

import com.acainfo.backend.student.domain.entity.Student;

/**
 * Interfaz que define las operaciones de actualización para la entidad Student.
 *
 * Las validaciones de integridad se manejan mediante constraints en la base de datos.
 * Las validaciones de formato se realizan en los DTOs de entrada.
 */
public interface UpdateStudentRepository {

    /**
     * Actualiza un estudiante existente.
     *
     * @param student el estudiante con los datos actualizados
     * @return el estudiante actualizado
     */
    Student update(Student student);

    /**
     * Verifica si un estudiante existe antes de intentar actualizarlo.
     * Útil para proporcionar mensajes de error más claros.
     */
    boolean existsById(Long id);
}