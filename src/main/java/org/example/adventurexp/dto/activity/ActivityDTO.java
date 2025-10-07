package org.example.adventurexp.dto.activity;

public record ActivityDTO(

        Long id,

        String name,

        String description,

        int ageLimit,

        double pricePerMinutePerPerson,

        int maxParticipants,

        int minParticipants
) {
}
