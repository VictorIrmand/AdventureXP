package org.example.adventurexp.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.adventurexp.model.Role;

public record UserUpdateDTO(

        long id,
        @NotBlank(message = "Username must not be blank")
        @Size(min = 3, max = 50, message = "Username length must be between 3 and 50 characters")
        String username,

        @NotBlank(message = "First name must not be blank")
        @Size(min = 2, max = 50, message = "First name length must be between 2 and 50 characters")
        String firstName,

        @NotBlank(message = "Last name must not be blank")
        @Size(min = 2, max = 50, message = "Last name length must be between 2 and 50 characters")
        String lastName,

        @NotBlank(message = "Email must not be blank")
        @Size(min = 5, max = 50, message = "Email length must be between 5 and 50 characters")
        @Email(message = "Must be a valid e-mail")
        String email,

        Role role,

        String rawPassword
) {
}
