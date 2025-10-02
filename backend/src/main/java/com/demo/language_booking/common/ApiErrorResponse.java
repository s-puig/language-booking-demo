package com.demo.language_booking.common;

import java.time.Instant;

public record ApiErrorResponse(String error, String message, String path, Instant timestamp) {
}
