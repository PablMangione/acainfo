package com.acainfo.backend.enrollment.domain.exception;

public class InvalidEnrollmentStatusException extends RuntimeException {
    public InvalidEnrollmentStatusException(String message) {
        super(message);
    }
}
