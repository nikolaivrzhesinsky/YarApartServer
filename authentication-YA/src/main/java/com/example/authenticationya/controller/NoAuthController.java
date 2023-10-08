package com.example.authenticationya.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RequestMapping("/noauth")
public class NoAuthController {

    @GetMapping("/test")
    public ResponseEntity<?> testNoAuth() {
        return ResponseEntity.ok("No auth complete");
    }


}
