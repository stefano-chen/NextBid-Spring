package com.stefano.nextbid.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UtilsController {

    @GetMapping("/whoami")
    public ResponseEntity<?> whoami() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
