package com.acainfo.backend.subject.application.imp;

import com.acainfo.backend.subject.application.UpdateSubjectUseCase;
import com.acainfo.backend.subject.application.exception.SubjectBusinessException;
import com.acainfo.backend.subject.domain.entity.Subject;
import com.acainfo.backend.subject.domain.exception.DuplicateSubjectException;
import com.acainfo.backend.subject.domain.exception.InvalidSubjectDataException;
import com.acainfo.backend.subject.domain.exception.SubjectNotFoundException;
import com.acainfo.backend.subject.domain.repository.ReadSubjectRepository;
import com.acainfo.backend.subject.domain.repository.UpdateSubjectRepository;
import com.acainfo.backend.subjectgroup.domain.repository.ReadSubjectGroupRepository;
import com.acainfo.backend.subjectgroup.domain.value.GroupStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del caso de uso para la actualización de asignaturas.
 *
 * Maneja actualizaciones completas, parciales y cambios de estado.
 * Valida reglas de negocio antes de aplicar cambios.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateSubjectUseCaseImp implements UpdateSubjectUseCase {

    private final UpdateSubjectRepository updateSubjectRepository;
    private final ReadSubjectRepository readSubjectRepository;
    private final ReadSubjectGroupRepository readSubjectGroupRepository;

    @Override
    @Transactional
    public Subject update(Subject subject) {
        log.info("Actualizando asignatura con ID: {}", subject.getId());

        // Validaciones básicas
        if (subject == null) {
            throw new IllegalArgumentException("La asignatura no puede ser null");
        }

        if (subject.getId() == null || subject.getId() <= 0) {
            throw new IllegalArgumentException("Se requiere un ID válido para actualizar");
        }

        // Verificar que la asignatura existe
        Subject existingSubject = readSubjectRepository.findById(subject.getId())
                .orElseThrow(() -> new SubjectNotFoundException(
                        "No se encontró la asignatura con ID: " + subject.getId()
                ));

        // Verificar si el cambio genera duplicados (si cambió nombre, carrera, año o cuatrimestre)
        if (hasChangedUniqueFields(existingSubject, subject)) {
            checkForDuplicates(subject);
        }

        // Limpiar el nombre si es necesario
        if (subject.getName() != null) {
            subject.setName(subject.getName().trim());
        }

        // Actualizar
        Subject updatedSubject = updateSubjectRepository.update(subject);

        log.info("Asignatura actualizada exitosamente: ID={}", updatedSubject.getId());
        return updatedSubject;
    }

    @Override
    @Transactional
    public Subject partialUpdate(Long id, Subject partialSubject) {
        log.info("Actualización parcial de asignatura con ID: {}", id);

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que 0");
        }

        // Obtener la asignatura existente
        Subject existingSubject = readSubjectRepository.findById(id)
                .orElseThrow(() -> new SubjectNotFoundException(
                        "No se encontró la asignatura con ID: " + id
                ));

        // Aplicar solo los campos no nulos
        Subject updatedSubject = applyPartialUpdate(existingSubject, partialSubject);

        // Verificar duplicados si cambió algún campo único
        if (hasChangedUniqueFields(existingSubject, updatedSubject)) {
            checkForDuplicates(updatedSubject);
        }

        // Actualizar
        Subject result = updateSubjectRepository.update(updatedSubject);

        log.info("Actualización parcial completada para asignatura ID: {}", id);
        return result;
    }

    @Override
    @Transactional
    public Subject updateActiveStatus(Long id, boolean active) {
        log.info("Cambiando estado de asignatura {} a {}", id, active ? "ACTIVA" : "INACTIVA");

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que 0");
        }

        // Obtener la asignatura
        Subject subject = readSubjectRepository.findById(id)
                .orElseThrow(() -> new SubjectNotFoundException(
                        "No se encontró la asignatura con ID: " + id
                ));

        // Si se intenta desactivar, verificar que no tenga grupos activos
        if (!active && hasActiveGroups(id)) {
            throw new SubjectBusinessException(
                    "No se puede desactivar la asignatura porque tiene grupos activos"
            );
        }

        // Cambiar el estado
        subject.setIsActive(active);
        Subject updatedSubject = updateSubjectRepository.update(subject);

        log.info("Estado de asignatura {} cambiado a {}", id, active ? "ACTIVA" : "INACTIVA");
        return updatedSubject;
    }

    @Override
    public boolean canBeUpdated(Subject subject) {
        if (subject == null || subject.getId() == null) {
            return false;
        }

        try {
            // Verificar que existe
            if (!readSubjectRepository.existsById(subject.getId())) {
                return false;
            }

            // Verificar que no genera duplicados
            Subject existing = readSubjectRepository.findById(subject.getId()).orElse(null);
            if (existing != null && hasChangedUniqueFields(existing, subject)) {
                var duplicates = readSubjectRepository.findByMajorAndCourseYearAndQuarter(
                        subject.getMajor(),
                        subject.getCourseYear(),
                        subject.getQuarter()
                );

                return duplicates.stream()
                        .noneMatch(s -> !s.getId().equals(subject.getId()) &&
                                s.getName().equalsIgnoreCase(subject.getName()));
            }

            return true;
        } catch (Exception e) {
            log.error("Error al verificar si se puede actualizar: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean canBeDeactivated(Long id) {
        if (id == null || id <= 0) {
            return false;
        }

        try {
            // Verificar que existe y está activa
            Subject subject = readSubjectRepository.findById(id).orElse(null);
            if (subject == null || !Boolean.TRUE.equals(subject.getIsActive())) {
                return false;
            }

            // Verificar que no tiene grupos activos
            return !hasActiveGroups(id);
        } catch (Exception e) {
            log.error("Error al verificar si se puede desactivar: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Verifica si han cambiado los campos que forman la clave única.
     */
    private boolean hasChangedUniqueFields(Subject existing, Subject updated) {
        return !existing.getName().equalsIgnoreCase(updated.getName()) ||
                !existing.getMajor().equals(updated.getMajor()) ||
                !existing.getCourseYear().equals(updated.getCourseYear()) ||
                !existing.getQuarter().equals(updated.getQuarter());
    }

    /**
     * Verifica que no exista otra asignatura con la misma combinación única.
     */
    private void checkForDuplicates(Subject subject) {
        var existingSubjects = readSubjectRepository.findByMajorAndCourseYearAndQuarter(
                subject.getMajor(),
                subject.getCourseYear(),
                subject.getQuarter()
        );

        boolean isDuplicate = existingSubjects.stream()
                .anyMatch(existing ->
                        !existing.getId().equals(subject.getId()) &&
                                existing.getName().equalsIgnoreCase(subject.getName())
                );

        if (isDuplicate) {
            throw new DuplicateSubjectException(
                    String.format("Ya existe otra asignatura '%s' en %s - %s año - %s cuatrimestre",
                            subject.getName(),
                            subject.getMajor(),
                            subject.getCourseYear(),
                            subject.getQuarter())
            );
        }
    }

    /**
     * Aplica los campos no nulos del objeto parcial al objeto existente.
     */
    private Subject applyPartialUpdate(Subject existing, Subject partial) {
        Subject.SubjectBuilder builder = Subject.builder()
                .id(existing.getId())
                .name(partial.getName() != null ? partial.getName().trim() : existing.getName())
                .major(partial.getMajor() != null ? partial.getMajor() : existing.getMajor())
                .courseYear(partial.getCourseYear() != null ? partial.getCourseYear() : existing.getCourseYear())
                .quarter(partial.getQuarter() != null ? partial.getQuarter() : existing.getQuarter())
                .isActive(partial.getIsActive() != null ? partial.getIsActive() : existing.getIsActive())
                .createdAt(existing.getCreatedAt()) // Preservar fecha de creación
                .updatedAt(existing.getUpdatedAt()); // Se actualizará automáticamente

        return builder.build();
    }

    /**
     * Verifica si una asignatura tiene grupos activos.
     */
    private boolean hasActiveGroups(Long subjectId) {
        var activeGroups = readSubjectGroupRepository.findBySubjectIdAndStatus(
                subjectId,
                GroupStatus.ACTIVE
        );

        boolean hasActive = !activeGroups.isEmpty();

        if (hasActive) {
            log.debug("La asignatura {} tiene {} grupos activos",
                    subjectId, activeGroups.size());
        }

        return hasActive;
    }
}