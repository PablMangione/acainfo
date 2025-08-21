package com.acainfo.backend.teacher.domain.repository;

import com.acainfo.backend.teacher.domain.entity.Teacher;

/**
 * Interfaz que define las operaciones de actualización para la entidad Teacher.
 *
 * Las validaciones de integridad se manejan mediante constraints en la base de datos.
 * Las validaciones de formato se realizan en los DTOs de entrada.
 */
public interface UpdateTeacherRepository {

    /**
     * Actualiza un profesor existente.
     *
     * @param teacher el profesor con los datos actualizados
     * @return el profesor actualizado
     */
    Teacher update(Teacher teacher);

    /**
     * Verifica si un profesor existe antes de intentar actualizarlo.
     * Útil para proporcionar mensajes de error más claros.
     */
    boolean existsById(Long id);
}