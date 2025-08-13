package com.acainfo.backend.groupsession.application;

import com.acainfo.backend.groupsession.infrastructure.controller.dto.GroupSessionInputDto;
import com.acainfo.backend.groupsession.infrastructure.controller.dto.GroupSessionOutputDto;

/**
 * Caso de uso para la creación de sesiones de grupo.
 * Define el contrato para la creación de nuevas sesiones en el sistema.
 */
public interface CreateGroupSessionUseCase {

    /**
     * Crea una nueva sesión de grupo en el sistema.
     *
     * @param inputDto datos de la sesión a crear
     * @return la sesión creada con su ID generado
     * @throws com.acainfo.backend.groupsession.domain.exception.DuplicateGroupSessionException
     *         si ya existe una sesión en ese horario para el grupo o aula
     * @throws com.acainfo.backend.groupsession.domain.exception.InvalidGroupSessionDataException
     *         si los datos proporcionados no son válidos
     * @throws com.acainfo.backend.subjectgroup.domain.exception.SubjectGroupNotFoundException
     *         si no existe el grupo especificado
     * @throws com.acainfo.backend.groupsession.application.exception.GroupSessionBusinessException
     *         si hay conflictos de horario o reglas de negocio
     */
    GroupSessionOutputDto create(GroupSessionInputDto inputDto);
}