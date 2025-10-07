package org.example.adventurexp.dto.user;

public record LoginRequestDTO(
        String username, // Both email and username
        String rawPassword
) {
}
