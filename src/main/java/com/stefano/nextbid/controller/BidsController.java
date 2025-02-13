package com.stefano.nextbid.controller;

import com.stefano.nextbid.dto.BidDTO;
import com.stefano.nextbid.dto.CreateAuctionBody;
import com.stefano.nextbid.dto.CreateBidBody;
import com.stefano.nextbid.exceptions.*;
import com.stefano.nextbid.service.BidService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class BidsController {

    private final BidService bidService;

    @Autowired
    public BidsController(BidService bidService) {
        this.bidService = bidService;
    }

    @GetMapping("/api/auctions/{id}/bids")
    public ResponseEntity<?> getAllBidsForAuctionById(@PathVariable Integer id) {
        List<BidDTO> bids = this.bidService.findByAuctionId(id);
        return new ResponseEntity<>(bids, HttpStatus.OK);
    }

    @PostMapping("/api/auctions/{id}/bids")
    public ResponseEntity<?> createBidForAuctionById(@PathVariable Integer id, @Valid @RequestBody CreateBidBody body) {
        this.bidService.createBid(id, body);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/bids{id}")
    public ResponseEntity<?> getBidDetailsById(@PathVariable Integer id) {
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

    @ExceptionHandler({NotAuthenticatedException.class, InvalidIdException.class, AuctionClosedException.class, AmountTooLowException.class, IllegalArgumentException.class})
    public ResponseEntity<?> handleNotAuthentication(Exception e) {
        return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
