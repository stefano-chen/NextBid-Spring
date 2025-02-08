package com.stefano.nextbid.dto;

import jakarta.validation.constraints.NotBlank;

public record SigninBody(
        @NotBlank(message = "username is required")
        String username,
        @NotBlank(message = "password is required")
        String password
) {
}
