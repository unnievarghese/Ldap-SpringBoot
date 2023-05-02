package com.sampleLdap.auth.Exception;

import lombok.AllArgsConstructor;
import org.springframework.security.core.parameters.P;

@AllArgsConstructor
public class Constants {

    public static final String SUCCESS = "success";
    public static final String FAILED = "failed";
    public static final String MESSAGE = "message";
    public static final String DATA = "data";

    public static final String LOGIN_FAILED = "Bad credentials!";

    public static final String WRONG_CRED = "Credentials provided are wrong!";

    public static final String USER_ADDED = "User added successfully.";

    public static final String BAD_REQUEST = "Bad request error!";
    public static final String JWT_TOKEN_CORRUPT = "Jwt token provided is corrupt or expired!";
}
