package com.acainfo.backend.subjectgroup.domain.exception;

/**
 * Excepción lanzada cuando se intenta inscribir a un estudiante
 * en un grupo que ya está completo.
 */
public class GroupCapacityExceededException extends RuntimeException {
    public GroupCapacityExceededException(String message) {
        super(message);
    }

    public GroupCapacityExceededException(Long groupId, Integer maxCapacity) {
        super(String.format("El grupo con ID %d ha alcanzado su capacidad máxima de %d estudiantes",
                groupId, maxCapacity));
    }
}