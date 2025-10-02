package org.example.adventurexp.service;

import lombok.RequiredArgsConstructor;
import org.example.adventurexp.dto.UserDTO;
import org.example.adventurexp.exception.UserNotFoundException;
import org.example.adventurexp.mapper.DTOMapper;
import org.example.adventurexp.model.User;
import org.example.adventurexp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserDTO getUserDTOById(long id) {
        try {

            User foundUser = userRepository.findById(id)
                    .orElseThrow(() -> {
                        return new UserNotFoundException("Fail to find user with id: " + id);
                    });

            return DTOMapper.toDTO(foundUser);
        } catch (DataAccessException e) {
            logger.error("Database problem: {}", e.getMessage());
            throw new IllegalStateException("Database problem: " + e.getMessage());
        }
    }

    public UserDTO getUserByUsername(String username) {
        try {

            User foundUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> {
                        return new UserNotFoundException("Fail to find user with username: " + username);
                    });

            return DTOMapper.toDTO(foundUser);
        } catch (DataAccessException e) {
            logger.error("Database problem: {}", e.getMessage());
            throw new IllegalStateException("Database problem: " + e.getMessage());
        }
    }

    public User getUserById(long id) {
        try {

            return userRepository.findById(id)
                    .orElseThrow(() -> {
                        return new UserNotFoundException("Fail to find user!");
                    });
        } catch (DataAccessException e) {
            logger.error("Database problem: {}", e.getMessage());
            throw new IllegalStateException("Database problem: " + e.getMessage());
        }
    }
}
