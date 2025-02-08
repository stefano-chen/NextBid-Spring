package com.stefano.nextbid.exceptions;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException() {
        super("This username is already taken.\nPlease try another one.");
    }
}
