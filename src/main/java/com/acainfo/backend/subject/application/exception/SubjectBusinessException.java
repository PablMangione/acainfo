package com.acainfo.backend.subject.application.exception;

/**
 * Excepción lanzada cuando una operación viola reglas de negocio
 * de la capa de aplicación para las asignaturas.
 *
 * Se diferencia de las excepciones de dominio en que estas
 * involucran lógica de coordinación entre agregados.
 */
public class SubjectBusinessException extends RuntimeException {

    public SubjectBusinessException(String message) {
        super(message);
    }

    public SubjectBusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}