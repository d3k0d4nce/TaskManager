package ru.kishko.taskmanager.authservice.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class DemoController {

    @GetMapping("/anon")
    public ResponseEntity<String> anonEndPoint() {
        return new ResponseEntity<>("Everyone can see this text", HttpStatus.OK);
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> usersEndPoint() {
        return new ResponseEntity<>("Only users can see this text", HttpStatus.OK);
    }

    @GetMapping("/producers")
    @PreAuthorize("hasRole('ROLE_PRODUCER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> producersEndPoint() {
        return new ResponseEntity<>("Only admins can see this text", HttpStatus.OK);
    }

}
