package com.richey.groupsplitpay.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(

        @NotBlank(message = "Username is Required")
        @NotNull String username,

        @NotBlank(message="Password is Required")
        @NotNull String password

) {
}
