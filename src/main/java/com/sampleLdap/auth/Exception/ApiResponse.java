package com.sampleLdap.auth.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Map;

@AllArgsConstructor
@Data
public class ApiResponse {

    private final Map responseData;

    private final String message;

    private final int status;
}
