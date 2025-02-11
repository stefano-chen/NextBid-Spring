package com.stefano.nextbid.dto;

import jakarta.validation.constraints.*;

import java.time.Instant;

public record CreateAuctionBody(
        @NotBlank(message = "title is required")
        String title,
        @NotBlank(message = "description is required")
        String description,
        @Positive(message = "invalid initial bid")
        double initialBid,
        @NotNull(message = "auction end date is required")
        @Future(message = "invalid auction end date")
        Instant dueDate
) {
}
