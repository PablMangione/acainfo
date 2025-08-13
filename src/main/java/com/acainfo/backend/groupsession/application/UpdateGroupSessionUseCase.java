package com.acainfo.backend.groupsession.application;

import com.acainfo.backend.groupsession.infrastructure.controller.dto.GroupSessionEditInputDto;
import com.acainfo.backend.groupsession.infrastructure.controller.dto.GroupSessionOutputDto;

/**
 * Caso de uso para la actualización de sesiones de grupo.
 * Define el contrato para las operaciones de modificación de sesiones existentes.
 */
public interface UpdateGroupSessionUseCase {

    /**
     * Actualiza una sesión existente.
     *
     * @param id el identificador de la sesión a actualizar
     * @param editDto los datos actualizados de la sesión
     * @return la sesión actualizada
     * @throws com.acainfo.backend.groupsession.domain.exception.GroupSessionNotFoundException
     *         si no se encuentra la sesión
     * @throws com.acainfo.backend.groupsession.domain.exception.DuplicateGroupSessionException
     *         si la actualización genera conflictos de horario
     * @throws com.acainfo.backend.groupsession.domain.exception.InvalidGroupSessionDataException
     *         si los datos proporcionados no son válidos
     * @throws com.acainfo.backend.groupsession.application.exception.GroupSessionBusinessException
     *         si hay conflictos de horario con profesores o estudiantes
     */
    GroupSessionOutputDto update(Long id, GroupSessionEditInputDto editDto);

    /**
     * Actualiza el tipo de una sesión (presencial, online, dual).
     *
     * @param id el identificador de la sesión
     * @param sessionType el nuevo tipo
     * @return la sesión actualizada
     * @throws com.acainfo.backend.groupsession.domain.exception.GroupSessionNotFoundException
     *         si no se encuentra la sesión
     */
    GroupSessionOutputDto updateType(Long id, com.acainfo.backend.groupsession.domain.value.SessionType sessionType);
}