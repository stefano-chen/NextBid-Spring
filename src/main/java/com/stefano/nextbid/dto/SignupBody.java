package com.stefano.nextbid.dto;

import jakarta.validation.constraints.NotBlank;

public record SignupBody(
        @NotBlank(message = "name is required")
        String name,
        @NotBlank(message = "surname is required")
        String surname,
        @NotBlank(message = "username is required")
        String username,
        @NotBlank(message = "password is required")
        String password
) {
}
