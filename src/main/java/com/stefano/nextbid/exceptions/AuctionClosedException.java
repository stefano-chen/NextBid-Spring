package com.stefano.nextbid.exceptions;

public class AuctionClosedException extends RuntimeException {
    public AuctionClosedException() {
        super("Auction Closed");
    }
}
