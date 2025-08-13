package com.acainfo.backend.subjectgroup.domain.exception;

import com.acainfo.backend.subjectgroup.domain.value.GroupStatus;

/**
 * Excepción lanzada cuando se intenta realizar una operación
 * no permitida para el estado actual del grupo.
 */
public class InvalidGroupStatusException extends RuntimeException {
    public InvalidGroupStatusException(String message) {
        super(message);
    }

    public InvalidGroupStatusException(Long groupId, GroupStatus currentStatus, String operation) {
        super(String.format("No se puede %s el grupo con ID %d en estado %s",
                operation, groupId, currentStatus));
    }
}