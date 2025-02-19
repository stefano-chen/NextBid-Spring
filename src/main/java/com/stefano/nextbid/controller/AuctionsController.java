package com.stefano.nextbid.controller;

import com.stefano.nextbid.dto.AuctionDTO;
import com.stefano.nextbid.dto.CreateAuctionBody;
import com.stefano.nextbid.dto.UpdateAuctionBody;
import com.stefano.nextbid.exceptions.InvalidIdException;
import com.stefano.nextbid.exceptions.NotAuthenticatedException;
import com.stefano.nextbid.exceptions.NotAuthorizedException;
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

    // Endpoint for creating an auction
    @PostMapping("")
    public ResponseEntity<?> createAuction(@Valid @RequestBody CreateAuctionBody body) {
        AuctionDTO createdAuction = this.auctionService.createAuction(body);
        return new ResponseEntity<>(createdAuction, HttpStatus.OK);
    }

    // Endpoint for getting a list of auctions that matches a query q
    @GetMapping("")
    public ResponseEntity<?> getAllAuctionsFilterByQuery(@RequestParam(value = "q", defaultValue = "", required = false) String q) {
        List<AuctionDTO> auctions = this.auctionService.getAllAuctions(q);
        return new ResponseEntity<>(auctions, HttpStatus.OK);
    }

    // Endpoint for getting the details of an auction identified by a given id
    @GetMapping("/{id}")
    public ResponseEntity<?> getAuctionDetailById(@PathVariable Integer id) {
        AuctionDTO auction = this.auctionService.getAuctionById(id);
        return new ResponseEntity<>(auction, HttpStatus.OK);
    }

    // Endpoint for updating some details of an auction identified by a given id
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAuctionById(@PathVariable Integer id, @RequestBody UpdateAuctionBody body) {
        this.auctionService.updateAuctionById(id, body);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Endpoint for deleting an auction identified by a given id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAuctionById(@PathVariable Integer id) {
        this.auctionService.deleteAuctionById(id);
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

    @ExceptionHandler({NotAuthenticatedException.class, InvalidIdException.class, NotAuthorizedException.class})
    public ResponseEntity<?> handleNotAuthentication(Exception e) {
        return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
