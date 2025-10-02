package com.demo.language_booking.common.exceptions;

public class UnavailableSessionException extends RuntimeException {
    public UnavailableSessionException(String message) {
        super(message);
    }
}
