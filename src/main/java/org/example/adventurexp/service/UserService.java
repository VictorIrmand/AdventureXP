package org.example.adventurexp.service;

import lombok.RequiredArgsConstructor;
import org.example.adventurexp.dto.SignUpRequestDTO;
import org.example.adventurexp.dto.UserDTO;
import org.example.adventurexp.exception.UserNotFoundException;
import org.example.adventurexp.mapper.DTOMapper;
import org.example.adventurexp.model.Role;
import org.example.adventurexp.model.User;
import org.example.adventurexp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final PasswordEncoder passwordEncoder;



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

    public UserDTO signUp(SignUpRequestDTO signUpRequestDTO){
        try {
            String hashedPassword = passwordEncoder.encode(signUpRequestDTO.rawPassword());
            User user = new User(
                    signUpRequestDTO.username(),
                    signUpRequestDTO.firstName(),
                    signUpRequestDTO.lastName(),
                    signUpRequestDTO.email()
            );
            user.changeRole(Role.CUSTOMER); // Altid en customer
            user.changePasswordHash(hashedPassword);
             return DTOMapper.toDTO(userRepository.save(user));
        } catch (DataIntegrityViolationException e) {
            logger.error("User with name; {} contains database constrains violations", signUpRequestDTO.username());
            throw new IllegalStateException("Database constrain violation; " + e.getMessage());
        }

        catch (DataAccessException e) {
            logger.error("Data access error while saving user; {}", signUpRequestDTO.username());
            throw new IllegalStateException("Database error; " + e.getMessage());
        }


    }

}
