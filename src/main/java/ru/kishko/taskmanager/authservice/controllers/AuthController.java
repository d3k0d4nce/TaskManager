package ru.kishko.taskmanager.authservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kishko.taskmanager.authservice.dtos.AuthenticationRequest;
import ru.kishko.taskmanager.authservice.dtos.AuthenticationResponse;
import ru.kishko.taskmanager.authservice.dtos.RegisterRequest;
import ru.kishko.taskmanager.authservice.services.AuthenticationService;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {

        return new ResponseEntity<>(authenticationService.register(request), HttpStatus.CREATED);

    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {

        return ResponseEntity.ok(authenticationService.authenticate(request));

    }

}
