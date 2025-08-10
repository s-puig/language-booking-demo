package com.demo.language_booking.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TokenParsingException extends RuntimeException {
    public TokenParsingException(String message) {
        super(message);
    }
}
