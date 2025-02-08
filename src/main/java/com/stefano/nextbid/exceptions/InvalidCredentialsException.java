package com.stefano.nextbid.exceptions;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Invalid username or password. Please try again.");
    }
}
