package org.example.adventurexp.dto;

import org.example.adventurexp.model.Role;

import java.time.LocalDateTime;

public record UserDTO (
        long id,
        String username,
        String firstName,
        String lastName,
        Role role,
        String email,
        String createdAt
){
}
