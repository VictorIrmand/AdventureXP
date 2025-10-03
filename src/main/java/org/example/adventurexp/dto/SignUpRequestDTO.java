package org.example.adventurexp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequestDTO(
        @NotBlank(message = "Username must not be blank")
        @Size(max = 50, message = "Username size must not exceed 50")
        String username,

        @NotBlank(message = "First name must not be blank")
        @Size(max = 50, message = "First name length must not exceed 50")
        String firstName,

        @NotBlank(message = "Last name must not be blank")
        @Size(max = 50, message = "Last name length must not exceed 50")
        String lastName,

        @NotBlank(message = "Name must not be blank")
        @Size(max = 50, message = "Size must not exceed 50")
        @Email(message = "Must be a valid e-mail")
        String email,

        // Laver vi senere @Size(message = "Size must")
        String rawPassword
) {




}
