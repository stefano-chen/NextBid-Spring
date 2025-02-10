package com.stefano.nextbid.exceptions;

public class NotAuthenticatedException extends RuntimeException {
    public NotAuthenticatedException() {
        super("Not Authenticated");
    }
}
