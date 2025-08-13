package com.acainfo.backend.auth.domain.exception;

import io.jsonwebtoken.ExpiredJwtException;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message, ExpiredJwtException e) {
        super(message);
    }
}
