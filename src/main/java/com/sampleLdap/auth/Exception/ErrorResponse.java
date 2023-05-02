package com.sampleLdap.auth.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorResponse {

    private final String message;

    private final int status;

    private final String customMessage;
}
