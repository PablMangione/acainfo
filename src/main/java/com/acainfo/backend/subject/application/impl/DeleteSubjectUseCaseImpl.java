package com.acainfo.backend.subject.application.impl;

import com.acainfo.backend.subject.application.DeleteSubjectUseCase;
import com.acainfo.backend.subject.application.exception.SubjectBusinessException;
import com.acainfo.backend.subject.domain.entity.Subject;
import com.acainfo.backend.subject.domain.exception.SubjectNotFoundException;
import com.acainfo.backend.subject.domain.repository.DeleteSubjectRepository;
import com.acainfo.backend.subject.domain.repository.ReadSubjectRepository;
import com.acainfo.backend.subject.domain.repository.UpdateSubjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del caso de uso para eliminar asignaturas.
 * Soporta tanto eliminación física (hard delete) como lógica (soft delete).
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteSubjectUseCaseImpl implements DeleteSubjectUseCase {

    private final DeleteSubjectRepository deleteRepository;
    private final ReadSubjectRepository readRepository;
    private final UpdateSubjectRepository updateRepository;

    /**
     * Elimina físicamente una asignatura del sistema.
     * Esta operación es irreversible y debe usarse con precaución.
     *
     * @param id el identificador de la asignatura a eliminar
     */
    @Override
    @Transactional
    public void deleteById(long id) {
        log.warn("Iniciando eliminación física de asignatura con ID: {}", id);

        // Verificar que la asignatura existe
        if (!readRepository.existsById(id)) {
            log.error("Intento de eliminar asignatura inexistente. ID: {}", id);
            throw new SubjectNotFoundException(
                    "No se puede eliminar. No existe asignatura con ID: " + id
            );
        }

        // Verificar que puede ser eliminada
        if (!canBeDeleted(id)) {
            log.error("Intento de eliminar asignatura con dependencias. ID: {}", id);
            throw new SubjectBusinessException(
                    "No se puede eliminar la asignatura porque tiene datos relacionados " +
                            "(grupos, inscripciones, etc.). Considere desactivarla en su lugar."
            );
        }

        // Proceder con la eliminación
        boolean deleted = deleteRepository.deleteById(id);

        if (deleted) {
            log.info("Asignatura eliminada físicamente. ID: {}", id);
        } else {
            log.error("Fallo inesperado al eliminar asignatura. ID: {}", id);
            throw new SubjectBusinessException(
                    "No se pudo eliminar la asignatura. Por favor, intente nuevamente."
            );
        }
    }

    /**
     * Elimina lógicamente una asignatura (soft delete).
     * La asignatura permanece en la base de datos pero se marca como inactiva.
     * Esta es la opción preferida para mantener la integridad histórica.
     *
     * @param id el identificador de la asignatura
     */
    @Override
    @Transactional
    public void softDeleteById(long id) {
        log.info("Iniciando eliminación lógica de asignatura con ID: {}", id);

        // Obtener la asignatura
        Subject subject = readRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Intento de desactivar asignatura inexistente. ID: {}", id);
                    return new SubjectNotFoundException(
                            "No se puede desactivar. No existe asignatura con ID: " + id
                    );
                });

        // Verificar que no esté ya inactiva
        if (!subject.isActive()) {
            log.warn("Intento de desactivar asignatura ya inactiva. ID: {}", id);
            throw new SubjectBusinessException(
                    "La asignatura ya está inactiva"
            );
        }

        // Validar que puede ser desactivada
        validateSoftDelete(subject);

        // Marcar como inactiva
        subject.setActive(false);

        // Persistir el cambio
        Subject updatedSubject = updateRepository.update(subject);

        log.info("Asignatura marcada como inactiva (soft delete). ID: {}, Nombre: {}",
                updatedSubject.getId(), updatedSubject.getName());
    }

    /**
     * Verifica si una asignatura puede ser eliminada físicamente.
     *
     * @param id el identificador de la asignatura
     * @return true si puede ser eliminada, false si tiene dependencias
     */
    @Override
    @Transactional(readOnly = true)
    public boolean canBeDeleted(long id) {
        log.debug("Verificando si la asignatura ID: {} puede ser eliminada", id);

        // Verificar que existe
        if (!readRepository.existsById(id)) {
            log.debug("Asignatura ID: {} no existe", id);
            return false;
        }

        // Aquí deberías verificar todas las dependencias:
        // - No tiene grupos asociados
        // - No tiene inscripciones históricas
        // - No tiene calificaciones registradas
        // - No está referenciada en solicitudes

        // Por ahora, implementación simplificada:
        // Solo permitir eliminar asignaturas inactivas sin validación adicional
        Subject subject = readRepository.findById(id).orElse(null);
        if (subject == null) {
            return false;
        }

        // Solo permitir eliminar si está inactiva
        boolean canDelete = !subject.isActive();

        if (!canDelete) {
            log.debug("Asignatura ID: {} no puede ser eliminada porque está activa", id);
        } else {
            log.debug("Asignatura ID: {} puede ser eliminada", id);
        }

        return canDelete;
    }

    /**
     * Valida las reglas de negocio para la eliminación lógica.
     *
     * @param subject la asignatura a validar
     */
    private void validateSoftDelete(Subject subject) {
        // Aquí podrías agregar validaciones específicas como:
        // - Verificar que no esté en período de inscripciones
        // - Verificar que no tenga exámenes programados
        // - Notificar a estudiantes afectados

        // Por ahora solo registramos warnings
        log.warn("Desactivando asignatura: {}. Verificar impacto en grupos y estudiantes.",
                subject.getName());

        // En un sistema real, podrías tener algo como:
        /*
        if (enrollmentService.hasActiveEnrollments(subject.getId())) {
            throw new SubjectBusinessException(
                "No se puede desactivar la asignatura porque tiene inscripciones activas"
            );
        }

        if (scheduleService.hasFutureClasses(subject.getId())) {
            throw new SubjectBusinessException(
                "No se puede desactivar la asignatura porque tiene clases programadas"
            );
        }
        */
    }
}