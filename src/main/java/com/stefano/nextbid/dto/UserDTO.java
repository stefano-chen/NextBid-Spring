package com.stefano.nextbid.dto;

import java.time.Instant;

public record UserDTO(
        Integer _id,
        String username,
        String name,
        String surname,
        String bio,
        Instant createdAt
) {
}
