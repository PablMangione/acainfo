package com.acainfo.backend.subjectgroup.domain.exception;

public class DuplicateGroupException extends RuntimeException {
    public DuplicateGroupException(String message) {
        super(message);
    }
}
