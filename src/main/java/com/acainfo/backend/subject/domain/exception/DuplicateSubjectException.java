package com.acainfo.backend.subject.domain.exception;

public class DuplicateSubjectException extends RuntimeException {
    public DuplicateSubjectException(String message) {
        super(message);
    }
}
