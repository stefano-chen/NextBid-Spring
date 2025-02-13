package com.stefano.nextbid.exceptions;

public class AmountTooLowException extends RuntimeException {
    public AmountTooLowException() {
        super("Amount too low");
    }
}
