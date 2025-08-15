package com.acainfo.backend.groupcreationrequest.domain.exception;

public class DuplicatePendingRequestException extends RuntimeException {
    public DuplicatePendingRequestException(String message) {
        super(message);
    }
}
