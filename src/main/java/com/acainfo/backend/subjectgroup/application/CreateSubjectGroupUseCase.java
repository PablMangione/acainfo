package com.acainfo.backend.subjectgroup.application;

import com.acainfo.backend.subjectgroup.infrastructure.controller.dto.SubjectGroupInputDto;
import com.acainfo.backend.subjectgroup.infrastructure.controller.dto.SubjectGroupOutputDto;

/**
 * Caso de uso para la creación de grupos de asignatura.
 * Define el contrato para la creación de nuevos grupos en el sistema.
 */
public interface CreateSubjectGroupUseCase {

    /**
     * Crea un nuevo grupo de asignatura en el sistema.
     *
     * @param inputDto datos del grupo a crear
     * @return el grupo creado con su ID generado
     * @throws com.acainfo.backend.subjectgroup.domain.exception.DuplicateGroupException
     *         si ya existe un grupo con el mismo nombre
     * @throws com.acainfo.backend.subjectgroup.domain.exception.InvalidSubjectGroupDataException
     *         si los datos proporcionados no son válidos
     * @throws com.acainfo.backend.subject.domain.exception.SubjectNotFoundException
     *         si no existe la asignatura especificada
     * @throws com.acainfo.backend.teacher.domain.exception.TeacherNotFoundException
     *         si no existe el profesor especificado
     */
    SubjectGroupOutputDto create(SubjectGroupInputDto inputDto);
}