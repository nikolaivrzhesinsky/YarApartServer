package com.example.authenticationya.controller;


import com.example.authenticationya.dto.request.LogInDto;
import com.example.authenticationya.dto.request.RegistrationDto;
import com.example.authenticationya.service.seviceInterface.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(
            @RequestBody RegistrationDto registrationDto) {
        authenticationService.registration(registrationDto);

        return ResponseEntity.ok("Create Success");
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LogInDto logInDto) {

        return ResponseEntity.ok(authenticationService.authentication(logInDto));
    }

    @GetMapping("/activate/{code}")
    public ResponseEntity<?> activation(@PathVariable String code) {

        if (authenticationService.activateUser(code)) {
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("https://ya.ru/")).build();
        } else return new ResponseEntity<>("Something go wrong", HttpStatus.SERVICE_UNAVAILABLE);
    }


}
