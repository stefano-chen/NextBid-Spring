package com.stefano.nextbid.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bids")
public class BidsController {

    @GetMapping("/{id}")
    public ResponseEntity<?> getBidDetailsById(@PathVariable Integer id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
