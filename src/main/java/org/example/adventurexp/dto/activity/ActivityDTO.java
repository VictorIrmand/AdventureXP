package org.example.adventurexp.dto.activity;

public record ActivityDTO(

        long id,

        String name,

        String description,

        int ageLimit,

        double pricePerMinutePerPerson,

        int maxParticipants,

        int minParticipants,

        String imgUrl
) {
}
