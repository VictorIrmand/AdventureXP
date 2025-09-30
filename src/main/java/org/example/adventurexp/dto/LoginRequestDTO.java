package org.example.adventurexp.dto;

public record LoginRequestDTO(
        String username, // Both email and username
        String rawPassword
) {
}
