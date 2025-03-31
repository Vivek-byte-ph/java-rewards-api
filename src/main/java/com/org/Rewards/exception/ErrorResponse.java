package com.org.Rewards.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {
    /**
     * Constructs an ErrorResponse object.
     *
     * @param status  The HTTP status code.
     * @param message The error message.
     * @param path    The request path.
     */
    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;

}
