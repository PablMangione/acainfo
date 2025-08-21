package com.acainfo.backend.subject.domain.repository;

import com.acainfo.backend.subject.domain.entity.Subject;

import java.util.Optional;

/**
 * Interfaz que define las operaciones de actualización para la entidad Subject.
 *
 * Las validaciones de integridad se manejan mediante constraints en la base de datos.
 * Las validaciones de formato se realizan en los DTOs de entrada.
 */
public interface UpdateSubjectRepository {

    /**
     * Actualiza una asignatura existente.
     *
     * @param subject la asignatura con los datos actualizados
     * @return la asignatura actualizada
     */
    Subject update(Subject subject);

    /**
     * Verifica si una asignatura existe antes de intentar actualizarla.
     * Útil para proporcionar mensajes de error más claros.
     */
    boolean existsById(Long id);
}