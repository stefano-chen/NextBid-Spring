package com.stefano.nextbid.dto;

import com.stefano.nextbid.entity.Auction;
import com.stefano.nextbid.entity.User;

import java.time.Instant;

public record BidDTO(
        Integer _id,
        User user,
        Auction auction,
        double amount,
        Instant createdAt
) {
}
