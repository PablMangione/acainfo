package com.acainfo.backend.student.domain.exception;

public class DuplicateStudentException extends RuntimeException {
    public DuplicateStudentException(String message) {
        super(message);
    }
}
