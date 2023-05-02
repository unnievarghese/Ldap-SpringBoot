package com.sampleLdap.auth.Exception;

import io.jsonwebtoken.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException.*;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class CustomException {
    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> forbiddenException(AccessDeniedException ex){
        ErrorResponse error = new ErrorResponse(Constants.LOGIN_FAILED, HttpStatus.FORBIDDEN.value(), Constants.WRONG_CRED);
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity<ErrorResponse> Exception(NullPointerException ex) {
        ErrorResponse error = new ErrorResponse(Constants.FAILED, HttpStatus.BAD_REQUEST.value(), Constants.BAD_REQUEST);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
