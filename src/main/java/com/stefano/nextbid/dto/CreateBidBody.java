package com.stefano.nextbid.dto;

import jakarta.validation.constraints.Positive;

public record CreateBidBody(
        @Positive(message = "amount must be positive and not zero")
        double amount
) {
}
