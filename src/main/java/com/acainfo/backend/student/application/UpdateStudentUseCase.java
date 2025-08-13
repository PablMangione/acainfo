package com.acainfo.backend.student.application;

import com.acainfo.backend.student.infrastructure.controller.dto.StudentEditInputDto;
import com.acainfo.backend.student.infrastructure.controller.dto.StudentOutputDto;
import com.acainfo.backend.student.infrastructure.controller.dto.StudentPasswordUpdateDto;

/**
 * Caso de uso para la actualización de estudiantes.
 * Define el contrato para las operaciones de modificación de estudiantes existentes.
 */
public interface UpdateStudentUseCase {

    /**
     * Actualiza los datos básicos de un estudiante existente.
     *
     * @param id el identificador del estudiante a actualizar
     * @param editDto los datos actualizados del estudiante
     * @return el estudiante actualizado
     * @throws com.acainfo.backend.student.domain.exception.StudentNotFoundException
     *         si no se encuentra el estudiante
     * @throws com.acainfo.backend.student.domain.exception.DuplicateStudentException
     *         si el nuevo email ya está en uso
     * @throws com.acainfo.backend.student.domain.exception.InvalidStudentDataException
     *         si los datos proporcionados no son válidos
     */
    StudentOutputDto update(Long id, StudentEditInputDto editDto);

    /**
     * Actualiza la contraseña de un estudiante.
     *
     * @param id el identificador del estudiante
     * @param passwordDto los datos de cambio de contraseña
     * @throws com.acainfo.backend.student.domain.exception.StudentNotFoundException
     *         si no se encuentra el estudiante
     * @throws com.acainfo.backend.student.application.exception.StudentBusinessException
     *         si la contraseña actual no es correcta o las nuevas no coinciden
     */
    void updatePassword(Long id, StudentPasswordUpdateDto passwordDto);

    /**
     * Activa o desactiva un estudiante.
     *
     * @param id el identificador del estudiante
     * @param active true para activar, false para desactivar
     * @return el estudiante con el estado actualizado
     * @throws com.acainfo.backend.student.domain.exception.StudentNotFoundException
     *         si no se encuentra el estudiante
     * @throws com.acainfo.backend.student.application.exception.StudentBusinessException
     *         si no se puede cambiar el estado debido a reglas de negocio
     */
    StudentOutputDto updateActiveStatus(Long id, boolean active);
}