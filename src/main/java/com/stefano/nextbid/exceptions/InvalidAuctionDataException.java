package com.stefano.nextbid.exceptions;

public class InvalidAuctionDataException extends RuntimeException {
    public InvalidAuctionDataException() {
        super("Invalid Auction Data");
    }
}
