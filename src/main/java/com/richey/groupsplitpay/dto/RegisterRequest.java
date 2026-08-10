package com.richey.groupsplitpay.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest(
        @NotBlank(message="Username is Required")
        @NotNull  String username,

//        @NotBlank(message = "Email is Required")
//        @NotNull String email,


        @NotBlank(message="Password is Required")
        @NotNull String password
) {
}
