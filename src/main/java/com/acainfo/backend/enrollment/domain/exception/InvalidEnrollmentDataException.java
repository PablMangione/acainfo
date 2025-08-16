package com.acainfo.backend.enrollment.domain.exception;

public class InvalidEnrollmentDataException extends RuntimeException {
    public InvalidEnrollmentDataException(String message) {
        super(message);
    }
}
