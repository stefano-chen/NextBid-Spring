package com.stefano.nextbid.controller;

import com.stefano.nextbid.entity.User;
import com.stefano.nextbid.repo.UserRepository;
import com.stefano.nextbid.service.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UtilsController {
    private final SessionManager sessionManager;
    private final UserRepository userRepository;

    @Autowired
    public UtilsController(SessionManager sessionManager, UserRepository userRepository) {
        this.sessionManager = sessionManager;
        this.userRepository = userRepository;
    }

    // Endpoint for checking if this session is authenticated
    @GetMapping("/whoami")
    public ResponseEntity<?> whoami() {
        if (!sessionManager.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        User user = userRepository.findById(sessionManager.getUserId()).get();
        return new ResponseEntity<>(Map.of("_id", user.getId(), "username", user.getUsername()), HttpStatus.OK);
    }
}
