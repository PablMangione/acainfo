package com.acainfo.backend.subjectgroup.application;

import com.acainfo.backend.subjectgroup.domain.value.GroupStatus;
import com.acainfo.backend.subjectgroup.infrastructure.controller.dto.SubjectGroupEditInputDto;
import com.acainfo.backend.subjectgroup.infrastructure.controller.dto.SubjectGroupOutputDto;

/**
 * Caso de uso para la actualización de grupos de asignatura.
 * Define el contrato para las operaciones de modificación de grupos existentes.
 */
public interface UpdateSubjectGroupUseCase {

    /**
     * Actualiza los datos básicos de un grupo existente.
     *
     * @param id el identificador del grupo a actualizar
     * @param editDto los datos actualizados del grupo
     * @return el grupo actualizado
     * @throws com.acainfo.backend.subjectgroup.domain.exception.SubjectGroupNotFoundException
     *         si no se encuentra el grupo
     * @throws com.acainfo.backend.subjectgroup.domain.exception.DuplicateGroupException
     *         si el nuevo nombre ya está en uso
     * @throws com.acainfo.backend.subjectgroup.domain.exception.InvalidSubjectGroupDataException
     *         si los datos proporcionados no son válidos
     * @throws com.acainfo.backend.subjectgroup.domain.exception.InvalidGroupStatusException
     *         si no se puede actualizar el grupo en su estado actual
     * @throws com.acainfo.backend.teacher.domain.exception.TeacherNotFoundException
     *         si no existe el nuevo profesor especificado
     */
    SubjectGroupOutputDto update(Long id, SubjectGroupEditInputDto editDto);

    /**
     * Actualiza el estado de un grupo.
     *
     * @param id el identificador del grupo
     * @param newStatus el nuevo estado
     * @return el grupo con el estado actualizado
     * @throws com.acainfo.backend.subjectgroup.domain.exception.SubjectGroupNotFoundException
     *         si no se encuentra el grupo
     * @throws com.acainfo.backend.subjectgroup.domain.exception.InvalidGroupStatusException
     *         si la transición de estado no es válida
     * @throws com.acainfo.backend.subjectgroup.application.exception.SubjectGroupBusinessException
     *         si no se puede cambiar el estado debido a reglas de negocio
     */
    SubjectGroupOutputDto updateStatus(Long id, GroupStatus newStatus);

    /**
     * Incrementa el contador de inscripciones cuando un estudiante se inscribe.
     * Este método es llamado internamente por el proceso de inscripción.
     *
     * @param groupId el identificador del grupo
     * @throws com.acainfo.backend.subjectgroup.domain.exception.SubjectGroupNotFoundException
     *         si no se encuentra el grupo
     * @throws com.acainfo.backend.subjectgroup.domain.exception.GroupCapacityExceededException
     *         si el grupo ya está completo
     * @throws com.acainfo.backend.subjectgroup.domain.exception.InvalidGroupStatusException
     *         si el grupo no está activo
     */
    void incrementEnrollmentCount(Long groupId);

    /**
     * Decrementa el contador de inscripciones cuando un estudiante cancela.
     * Este método es llamado internamente por el proceso de cancelación.
     *
     * @param groupId el identificador del grupo
     * @throws com.acainfo.backend.subjectgroup.domain.exception.SubjectGroupNotFoundException
     *         si no se encuentra el grupo
     * @throws com.acainfo.backend.subjectgroup.domain.exception.InvalidSubjectGroupDataException
     *         si el contador ya está en 0
     */
    void decrementEnrollmentCount(Long groupId);

    /**
     * Actualiza la capacidad máxima de un grupo.
     * Valida que la nueva capacidad no sea menor que las inscripciones actuales.
     *
     * @param id el identificador del grupo
     * @param newCapacity la nueva capacidad máxima
     * @return el grupo actualizado
     * @throws com.acainfo.backend.subjectgroup.domain.exception.SubjectGroupNotFoundException
     *         si no se encuentra el grupo
     * @throws com.acainfo.backend.subjectgroup.domain.exception.InvalidSubjectGroupDataException
     *         si la nueva capacidad es menor que las inscripciones actuales
     */
    SubjectGroupOutputDto updateCapacity(Long id, Integer newCapacity);
}