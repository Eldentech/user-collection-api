package com.eldentech.user.dto;

import com.eldentech.user.domain.enity.Sex;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserDTO(
        String id,
        @NotNull(message = "Name cannot be empty.")
        @Size(min = 3, max = 70, message = "Name value can be between 3 characters and 70 characters length")
        String name,
        @NotNull(message = "Sex cannot be null.")
        Sex sex,
        @NotNull(message = "Age cannot be null.")
        int age,
        @NotNull(message = "Country cannot be null.")
        @Size(min = 3, max = 3, message = "Country code should be a ISO 3166 international standard Alpha-3 code")
        String country
) {
}
