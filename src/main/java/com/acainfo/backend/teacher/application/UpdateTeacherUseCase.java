package com.acainfo.backend.teacher.application;

import com.acainfo.backend.teacher.infrastructure.controller.dto.TeacherEditInputDto;
import com.acainfo.backend.teacher.infrastructure.controller.dto.TeacherOutputDto;
import com.acainfo.backend.teacher.infrastructure.controller.dto.TeacherPasswordUpdateDto;

/**
 * Caso de uso para la actualización de profesores.
 * Define el contrato para las operaciones de modificación de profesores existentes.
 */
public interface UpdateTeacherUseCase {

    /**
     * Actualiza los datos básicos de un profesor existente.
     *
     * @param id el identificador del profesor a actualizar
     * @param editDto los datos actualizados del profesor
     * @return el profesor actualizado
     * @throws com.acainfo.backend.teacher.domain.exception.TeacherNotFoundException
     *         si no se encuentra el profesor
     * @throws com.acainfo.backend.teacher.domain.exception.DuplicateTeacherException
     *         si el nuevo email ya está en uso
     * @throws com.acainfo.backend.teacher.domain.exception.InvalidTeacherDataException
     *         si los datos proporcionados no son válidos
     */
    TeacherOutputDto update(Long id, TeacherEditInputDto editDto);

    /**
     * Actualiza la contraseña de un profesor.
     *
     * @param id el identificador del profesor
     * @param passwordDto los datos de cambio de contraseña
     * @throws com.acainfo.backend.teacher.domain.exception.TeacherNotFoundException
     *         si no se encuentra el profesor
     * @throws com.acainfo.backend.teacher.application.exception.TeacherBusinessException
     *         si la contraseña actual no es correcta o las nuevas no coinciden
     */
    void updatePassword(Long id, TeacherPasswordUpdateDto passwordDto);

    /**
     * Asigna o revoca el rol de administrador a un profesor.
     * Solo puede ser ejecutado por un administrador.
     *
     * @param id el identificador del profesor
     * @param isAdmin true para asignar rol admin, false para revocarlo
     * @return el profesor con el rol actualizado
     * @throws com.acainfo.backend.teacher.domain.exception.TeacherNotFoundException
     *         si no se encuentra el profesor
     * @throws com.acainfo.backend.teacher.application.exception.TeacherBusinessException
     *         si se intenta revocar el admin del último administrador
     */
    TeacherOutputDto updateAdminStatus(Long id, boolean isAdmin);
}