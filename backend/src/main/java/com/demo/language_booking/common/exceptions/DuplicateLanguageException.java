package com.demo.language_booking.common.exceptions;

public class DuplicateLanguageException extends RuntimeException {
    public DuplicateLanguageException(String message) {
        super(message);
    }
}
