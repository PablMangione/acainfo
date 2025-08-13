package com.acainfo.backend.student.application;

import com.acainfo.backend.student.infrastructure.controller.dto.StudentInputDto;
import com.acainfo.backend.student.infrastructure.controller.dto.StudentOutputDto;

/**
 * Caso de uso para la creación de estudiantes.
 * Define el contrato para la creación de nuevos estudiantes en el sistema.
 */
public interface CreateStudentUseCase {

    /**
     * Crea un nuevo estudiante en el sistema.
     *
     * @param inputDto datos del estudiante a crear
     * @return el estudiante creado con su ID generado
     * @throws com.acainfo.backend.student.domain.exception.DuplicateStudentException
     *         si ya existe un estudiante con el mismo email
     * @throws com.acainfo.backend.student.domain.exception.InvalidStudentDataException
     *         si los datos proporcionados no son válidos
     */
    StudentOutputDto create(StudentInputDto inputDto);
}