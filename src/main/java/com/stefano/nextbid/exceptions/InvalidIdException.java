package com.stefano.nextbid.exceptions;

public class InvalidIdException extends RuntimeException {
    public InvalidIdException() {
        super("Invalid ID. Please check the ID and try again");
    }
}
