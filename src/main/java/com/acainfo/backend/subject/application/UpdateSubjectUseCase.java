package com.acainfo.backend.subject.application;

import com.acainfo.backend.subject.infrastructure.controller.dto.SubjectEditInputDto;
import com.acainfo.backend.subject.infrastructure.controller.dto.SubjectOutputDto;

/**
 * Caso de uso para la actualización de asignaturas.
 * Define el contrato para las operaciones de modificación de asignaturas existentes.
 */
public interface UpdateSubjectUseCase {

    /**
     * Actualiza una asignatura existente.
     *
     * @param id el identificador de la asignatura a actualizar
     * @param editDto los datos actualizados de la asignatura
     * @return la asignatura actualizada
     * @throws com.acainfo.backend.subject.domain.exception.SubjectNotFoundException
     *         si no se encuentra la asignatura
     * @throws com.acainfo.backend.subject.domain.exception.DuplicateSubjectException
     *         si la actualización genera una duplicación
     * @throws com.acainfo.backend.subject.domain.exception.InvalidSubjectDataException
     *         si los datos proporcionados no son válidos
     */
    SubjectOutputDto update(Long id, SubjectEditInputDto editDto);

    /**
     * Activa o desactiva una asignatura.
     *
     * @param id el identificador de la asignatura
     * @param active true para activar, false para desactivar
     * @return la asignatura con el estado actualizado
     * @throws com.acainfo.backend.subject.domain.exception.SubjectNotFoundException
     *         si no se encuentra la asignatura
     * @throws com.acainfo.backend.subject.application.exception.SubjectBusinessException
     *         si no se puede cambiar el estado debido a reglas de negocio
     */
    SubjectOutputDto updateActiveStatus(Long id, boolean active);
}