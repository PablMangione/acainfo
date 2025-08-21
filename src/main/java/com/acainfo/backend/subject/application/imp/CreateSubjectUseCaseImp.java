package com.acainfo.backend.subject.application.imp;

import com.acainfo.backend.subject.application.CreateSubjectUseCase;
import com.acainfo.backend.subject.domain.entity.Subject;
import com.acainfo.backend.subject.domain.exception.DuplicateSubjectException;
import com.acainfo.backend.subject.domain.exception.InvalidSubjectDataException;
import com.acainfo.backend.subject.domain.repository.CreateSubjectRepository;
import com.acainfo.backend.subject.domain.repository.ReadSubjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del caso de uso para la creación de asignaturas.
 *
 * Trabaja con entidades de dominio. Las validaciones de formato
 * (null, blank, size) ya vienen garantizadas desde el DTO en el controller.
 * Este caso de uso se centra en reglas de negocio y coordinación.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CreateSubjectUseCaseImp implements CreateSubjectUseCase {

    private final CreateSubjectRepository createSubjectRepository;
    private final ReadSubjectRepository readSubjectRepository;

    @Override
    @Transactional
    public Subject create(Subject subject) {
        log.info("Creando nueva asignatura: {}", subject.getName());

        // Validación defensiva mínima (por si se llama desde otro lugar que no sea controller)
        if (subject == null) {
            throw new IllegalArgumentException("La asignatura no puede ser null");
        }

        if (subject.getId() != null && subject.getId() != 0L) {
            throw new IllegalArgumentException("No se debe proporcionar ID al crear una asignatura");
        }

        // REGLA DE NEGOCIO: Verificar que no exista duplicado
        checkForDuplicates(subject);

        // Limpiar espacios del nombre si es necesario
        if (subject.getName() != null) {
            subject.setName(subject.getName().trim());
        }

        // Persistir (el ID lo genera la BD, isActive se pone en @PrePersist)
        Subject savedSubject = createSubjectRepository.save(subject);

        log.info("Asignatura creada con ID: {}", savedSubject.getId());
        return savedSubject;
    }

    /**
     * Regla de negocio: No pueden existir dos asignaturas con el mismo nombre
     * en la misma carrera, año y cuatrimestre.
     */
    private void checkForDuplicates(Subject subject) {
        var existingSubjects = readSubjectRepository.findByMajorAndCourseYearAndQuarter(
                subject.getMajor(),
                subject.getCourseYear(),
                subject.getQuarter()
        );

        boolean isDuplicate = existingSubjects.stream()
                .anyMatch(existing -> existing.getName().equalsIgnoreCase(subject.getName()));

        if (isDuplicate) {
            throw new DuplicateSubjectException(
                    String.format("Ya existe la asignatura '%s' en %s - %s año - %s cuatrimestre",
                            subject.getName(),
                            subject.getMajor(),
                            subject.getCourseYear(),
                            subject.getQuarter())
            );
        }
    }
}