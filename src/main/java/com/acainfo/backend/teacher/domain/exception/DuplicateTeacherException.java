package com.acainfo.backend.teacher.domain.exception;

public class DuplicateTeacherException extends RuntimeException {
    public DuplicateTeacherException(String message) {
        super(message);
    }
}
