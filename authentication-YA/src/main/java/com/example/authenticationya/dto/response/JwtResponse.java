package com.example.authenticationya.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private String userName;
    private List<String> role;


    public JwtResponse(String token, String userName, List<String> role) {
        this.token = token;
        this.userName = userName;
        this.role = role;
    }
}
