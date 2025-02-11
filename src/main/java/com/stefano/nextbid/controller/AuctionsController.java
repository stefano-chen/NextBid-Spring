package com.stefano.nextbid.controller;

import com.stefano.nextbid.dto.AuctionDTO;
import com.stefano.nextbid.dto.CreateAuctionBody;
import com.stefano.nextbid.exceptions.InvalidIdException;
import com.stefano.nextbid.exceptions.NotAuthenticatedException;
import com.stefano.nextbid.service.AuctionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auctions")
public class AuctionsController {

    private final AuctionService auctionService;

    @Autowired
    public AuctionsController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @PostMapping("")
    public ResponseEntity<?> createAuction(@Valid @RequestBody CreateAuctionBody body) {
        AuctionDTO createdAuction = this.auctionService.createAuction(body);
        return new ResponseEntity<>(createdAuction, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllAuctionsFilterByQuery(@RequestParam(value = "q", defaultValue = "", required = false) String q) {
        List<AuctionDTO> auctions = this.auctionService.getAllAuctions(q);
        return new ResponseEntity<>(auctions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAuctionDetailById(@PathVariable Integer id) {
        AuctionDTO auction = this.auctionService.getAuctionById(id);
        return new ResponseEntity<>(auction, HttpStatus.OK);
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleInvalidData(MethodArgumentNotValidException e) {
        List<String> errors = new ArrayList<>();

        e.getBindingResult().getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));

        return new ResponseEntity<>(Map.of("error", errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMissingBody(HttpMessageNotReadableException e) {
        return "Missing/Invalid HTTP body";
    }

    @ExceptionHandler({NotAuthenticatedException.class, InvalidIdException.class})
    public ResponseEntity<?> handleNotAuthentication(Exception e) {
        return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
