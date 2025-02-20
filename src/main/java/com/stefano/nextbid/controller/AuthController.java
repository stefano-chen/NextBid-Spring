package com.stefano.nextbid.controller;

import com.stefano.nextbid.dto.SigninBody;
import com.stefano.nextbid.dto.SignupBody;
import com.stefano.nextbid.dto.UserDTO;
import com.stefano.nextbid.exceptions.InvalidCredentialsException;
import com.stefano.nextbid.exceptions.UsernameAlreadyExistsException;
import com.stefano.nextbid.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService service) {
        this.authService = service;
    }

    // Endpoint for a user's signup
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupBody body) {
        UserDTO createdUser = this.authService.signup(body);
        return new ResponseEntity<>(createdUser, HttpStatus.OK);
    }

    // Endpoint for a user's signin
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@Valid @RequestBody SigninBody body) {
        UserDTO authenticatedUser = this.authService.signin(body);
        return new ResponseEntity<>(authenticatedUser, HttpStatus.OK);
    }

    // Endpoint for invalidate a session a.k.a. logout
    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        authService.logout();
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @ExceptionHandler({UsernameAlreadyExistsException.class, InvalidCredentialsException.class})
    public ResponseEntity<?> handleSignFail(Exception e) {
        return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationFail(MethodArgumentNotValidException e) {
        List<String> errors = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
        return new ResponseEntity<>(Map.of("error", errors), HttpStatus.BAD_REQUEST);
    }
}
