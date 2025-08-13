package com.acainfo.backend.groupsession.domain.exception;

public class DuplicateGroupSessionException extends RuntimeException {
    public DuplicateGroupSessionException(String message) {
        super(message);
    }
}
