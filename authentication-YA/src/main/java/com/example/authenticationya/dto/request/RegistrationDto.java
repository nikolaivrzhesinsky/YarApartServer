package com.example.authenticationya.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistrationDto {

    private String userName;
    private String email;
    private String password;
}
