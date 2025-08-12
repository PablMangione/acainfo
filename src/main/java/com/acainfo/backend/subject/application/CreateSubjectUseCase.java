package com.acainfo.backend.subject.application;

import com.acainfo.backend.subject.infrastructure.controller.dto.SubjectInputDto;
import com.acainfo.backend.subject.infrastructure.controller.dto.SubjectOutputDto;

/**
 * Caso de uso para la creación de asignaturas.
 * Define el contrato para la creación de nuevas asignaturas en el sistema.
 */
public interface CreateSubjectUseCase {

    /**
     * Crea una nueva asignatura en el sistema.
     *
     * @param inputDto datos de la asignatura a crear
     * @return la asignatura creada con su ID generado
     * @throws com.acainfo.backend.subject.domain.exception.DuplicateSubjectException
     *         si ya existe una asignatura con la misma combinación única
     * @throws com.acainfo.backend.subject.domain.exception.InvalidSubjectDataException
     *         si los datos proporcionados no son válidos
     */
    SubjectOutputDto create(SubjectInputDto inputDto);
}