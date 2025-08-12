package com.acainfo.backend.subject.application.impl;

import com.acainfo.backend.subject.application.CreateSubjectUseCase;
import com.acainfo.backend.subject.application.mapper.SubjectMapper;
import com.acainfo.backend.subject.domain.entity.Subject;
import com.acainfo.backend.subject.domain.repository.CreateSubjectRepository;
import com.acainfo.backend.subject.infrastructure.controller.dto.SubjectInputDto;
import com.acainfo.backend.subject.infrastructure.controller.dto.SubjectOutputDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del caso de uso para crear asignaturas.
 * Coordina la lógica de negocio para la creación de nuevas asignaturas.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CreateSubjectUseCaseImpl implements CreateSubjectUseCase {

    private final CreateSubjectRepository createRepository;
    private final SubjectMapper mapper;

    /**
     * Crea una nueva asignatura en el sistema.
     *
     * @param inputDto datos de la asignatura a crear
     * @return la asignatura creada con su ID generado
     */
    @Override
    @Transactional
    public SubjectOutputDto create(SubjectInputDto inputDto) {
        log.info("Iniciando creación de asignatura: {}", inputDto.getName());

        // Convertir DTO a entidad de dominio
        Subject subject = mapper.toDomain(inputDto);

        // Aplicar reglas de negocio adicionales si es necesario
        applyBusinessRules(subject);

        // Persistir a través del repositorio
        Subject savedSubject = createRepository.save(subject);

        log.info("Asignatura creada exitosamente: {} con ID: {}",
                savedSubject.getName(), savedSubject.getId());

        // Convertir a DTO de salida y retornar
        return mapper.toOutputDto(savedSubject);
    }

    /**
     * Aplica reglas de negocio específicas antes de crear la asignatura.
     *
     * @param subject la asignatura a validar y procesar
     */
    private void applyBusinessRules(Subject subject) {
        // Por ahora no hay reglas de negocio adicionales específicas
        // Aquí podrías agregar lógica como:
        // - Validar que no haya más de X asignaturas por año
        // - Verificar prerrequisitos
        // - Aplicar reglas específicas por carrera
        // - Normalizar el nombre (capitalización, etc.)

        // Ejemplo de normalización del nombre
        if (subject.getName() != null) {
            subject.setName(subject.getName().trim());
        }

        log.debug("Reglas de negocio aplicadas para la asignatura: {}", subject.getName());
    }
}