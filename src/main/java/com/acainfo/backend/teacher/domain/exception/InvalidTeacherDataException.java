package com.acainfo.backend.teacher.domain.exception;

public class InvalidTeacherDataException extends RuntimeException {
    public InvalidTeacherDataException(String message) {
        super(message);
    }
}
