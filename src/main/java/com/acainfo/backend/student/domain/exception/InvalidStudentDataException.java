package com.acainfo.backend.student.domain.exception;

public class InvalidStudentDataException extends RuntimeException {
    public InvalidStudentDataException(String message) {
        super(message);
    }
}
