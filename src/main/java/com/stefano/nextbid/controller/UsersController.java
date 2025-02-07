package com.stefano.nextbid.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @GetMapping("")
    public ResponseEntity<?> getAllUsersFilterByQuery(@RequestParam String q) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserDetailById(@PathVariable Integer id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/bio")
    public ResponseEntity<?> updateUserBio() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
