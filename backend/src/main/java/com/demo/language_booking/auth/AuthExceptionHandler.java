package com.demo.language_booking.auth;

import com.demo.language_booking.common.ApiErrorResponse;
import com.demo.language_booking.common.exceptions.InvalidPolicyHandlerException;
import com.demo.language_booking.common.exceptions.MalformedAuthorizeAnnotationException;
import com.demo.language_booking.common.exceptions.PermissionDeniedException;
import com.demo.language_booking.common.exceptions.UnavailableSessionException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class AuthExceptionHandler {
    @ExceptionHandler(UnavailableSessionException.class)
    public ResponseEntity<ApiErrorResponse> handleUnavailableSessionException(UnavailableSessionException e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiErrorResponse("Session Unavailable", e.getMessage(), request.getRequestURI(), Instant.now()));
    }

    @ExceptionHandler(MalformedAuthorizeAnnotationException.class)
    public ResponseEntity<ApiErrorResponse> handleMalformedAuthorizeAnnotationException(MalformedAuthorizeAnnotationException e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiErrorResponse("Malformed Authorize annotation", e.getMessage(), request.getRequestURI(), Instant.now()));
    }

    @ExceptionHandler(InvalidPolicyHandlerException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidPolicyHandlerException(InvalidPolicyHandlerException e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiErrorResponse("Invalid Policy Handler", e.getMessage(), request.getRequestURI(), Instant.now()));
    }

    @ExceptionHandler(PermissionDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handlePermissionDeniedException(PermissionDeniedException e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiErrorResponse("Permission denied", e.getMessage(), request.getRequestURI(), Instant.now()));
    }


}
