package com.demo.language_booking.common.exceptions;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidAuthException extends RuntimeException {
    public InvalidAuthException(String message) {
        super(message);
    }
}
