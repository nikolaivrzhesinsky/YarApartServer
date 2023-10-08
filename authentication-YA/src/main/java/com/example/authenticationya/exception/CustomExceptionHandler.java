package com.example.authenticationya.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorMessage> handleAuthenticationException(Exception ex) {

        ErrorMessage message = new ErrorMessage(HttpStatus.UNAUTHORIZED.value(),
                "Authentication failed at controller advice");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<ErrorMessage> handleAlreadyExistException(Exception ex) {

        ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST.value(),
                "User already exist");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
}
