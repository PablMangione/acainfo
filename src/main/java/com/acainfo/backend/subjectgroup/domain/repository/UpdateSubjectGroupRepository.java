package com.acainfo.backend.subjectgroup.domain.repository;

import com.acainfo.backend.subjectgroup.domain.entity.SubjectGroup;

/**
 * Interfaz que define las operaciones de actualización para la entidad SubjectGroup.
 *
 * Las validaciones de integridad se manejan mediante constraints en la base de datos.
 * Las validaciones de formato se realizan en los DTOs de entrada.
 */
public interface UpdateSubjectGroupRepository {

    /**
     * Actualiza un grupo existente.
     *
     * @param subjectGroup el grupo con los datos actualizados
     * @return el grupo actualizado
     */
    SubjectGroup update(SubjectGroup subjectGroup);

    /**
     * Incrementa el contador de inscripciones actuales.
     * Útil cuando un estudiante se inscribe.
     *
     * @param groupId el ID del grupo
     * @return true si se actualizó correctamente
     */
    boolean incrementEnrollmentCount(Long groupId);

    /**
     * Decrementa el contador de inscripciones actuales.
     * Útil cuando un estudiante cancela su inscripción.
     *
     * @param groupId el ID del grupo
     * @return true si se actualizó correctamente
     */
    boolean decrementEnrollmentCount(Long groupId);

    /**
     * Verifica si un grupo existe antes de intentar actualizarlo.
     */
    boolean existsById(Long id);
}