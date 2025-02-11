package com.stefano.nextbid.exceptions;

public class NotAuthorizedException extends RuntimeException {
    public NotAuthorizedException() {
        super("Not Authorized");
    }
}
