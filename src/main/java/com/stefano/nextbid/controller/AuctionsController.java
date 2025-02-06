package com.stefano.nextbid.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auctions")
public class AuctionsController {

    @GetMapping("")
    public ResponseEntity<?> getAllAuctionsFilterByQuery(@RequestParam String q) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> createAuction() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAuctionDetailById(@PathVariable Integer id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAuctionById(@PathVariable Integer id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAuctionById(@PathVariable Integer id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}/bids")
    public ResponseEntity<?> getAllBidsForAuctionById(@PathVariable Integer id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/bids")
    public ResponseEntity<?> createBidForAuctionById(@PathVariable Integer id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
