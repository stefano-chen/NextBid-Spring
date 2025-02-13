package com.stefano.nextbid.dto;

import jakarta.validation.constraints.Positive;

public record CreateBidBody(
        @Positive
        double amount
) {
}
