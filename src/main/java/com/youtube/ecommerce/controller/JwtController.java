package com.youtube.ecommerce.controller;

import com.youtube.ecommerce.entity.JwtRequest;
import com.youtube.ecommerce.entity.JwtResponse;
import com.youtube.ecommerce.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class JwtController {

    @Autowired
    private JwtService jwtService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createJwtToken(@RequestBody JwtRequest jwtRequest) {
        try {
            JwtResponse jwtResponse = jwtService.createJwtToken(jwtRequest);
            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if (errorMessage.contains("User not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
            } else if (errorMessage.contains("Invalid credentials")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
            }
        }
    }
}
