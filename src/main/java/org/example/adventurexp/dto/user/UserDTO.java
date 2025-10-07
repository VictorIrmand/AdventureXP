package org.example.adventurexp.dto.user;

import org.example.adventurexp.model.Role;

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
