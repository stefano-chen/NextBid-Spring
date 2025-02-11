package com.stefano.nextbid.dto;

import com.stefano.nextbid.entity.User;

import java.time.Instant;

public record AuctionDTO(
        Integer _id,
        String title,
        String description,
        double initialBid,
        Instant dueDate,
        Instant createdAt,
        User owner,
        User winner
) {
}
