package com.acainfo.backend.enrollment.application.exception;

/**
 * Excepción lanzada cuando ocurre un error relacionado con el procesamiento de pagos.
 * Por ejemplo: pago rechazado, token inválido, timeout en la pasarela de pago, etc.
 */
public class PaymentException extends RuntimeException {
    public PaymentException(String message) {
        super(message);
    }

    public PaymentException(String message, Throwable cause) {
        super(message, cause);
    }
}