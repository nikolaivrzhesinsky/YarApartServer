package com.example.authenticationya.exception;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ErrorMessage {

    private int statusCode;
    private String message;
}
