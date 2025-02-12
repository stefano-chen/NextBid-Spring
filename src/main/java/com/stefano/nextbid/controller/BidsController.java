package com.stefano.nextbid.controller;

import com.stefano.nextbid.dto.BidDTO;
import com.stefano.nextbid.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<?> createBidForAuctionById(@PathVariable Integer id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/bids{id}")
    public ResponseEntity<?> getBidDetailsById(@PathVariable Integer id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
