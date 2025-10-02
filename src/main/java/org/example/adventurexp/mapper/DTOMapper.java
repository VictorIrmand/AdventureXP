package org.example.adventurexp.mapper;

import org.example.adventurexp.dto.UserDTO;
import org.example.adventurexp.model.User;

public class DTOMapper {

    // user

    public static UserDTO toDTO (User user) {
        return new UserDTO (
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }

}
