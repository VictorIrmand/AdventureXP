package org.example.adventurexp.dto.activity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateActivityDTO(
        @NotBlank(message = "Activity name must not be blank")
        @Size(min = 3, max = 50, message = "Activity name-length must be between 3 and 50 characters")
        String name,
        @NotBlank(message = "Description must not be blank")
        @Size(min = 3, message = "Description must be at least 3 characters")
        @Size(max = 300, message = "Description must be under 300 characters")
        String description,

        int ageLimit,

        double pricePerMinutePerPerson,

        int maxParticipants,

        int minParticipants,

        String imgUrl
) {
}
