package com.example.authenticationya.exception;

import java.io.Serial;

public class AlreadyExistException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public AlreadyExistException(String msg) {
        super(msg);
    }

}
