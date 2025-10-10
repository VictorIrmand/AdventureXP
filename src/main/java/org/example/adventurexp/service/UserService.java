package org.example.adventurexp.service;

import lombok.RequiredArgsConstructor;
import org.example.adventurexp.dto.user.AdminSignupDTO;
import org.example.adventurexp.dto.user.SignUpRequestDTO;
import org.example.adventurexp.dto.user.UserDTO;
import org.example.adventurexp.dto.user.UserUpdateDTO;
import org.example.adventurexp.exception.DatabaseAccessException;
import org.example.adventurexp.exception.DuplicateResourceException;
import org.example.adventurexp.exception.NotFoundException;
import org.example.adventurexp.mapper.DTOMapper;
import org.example.adventurexp.model.Role;
import org.example.adventurexp.model.User;
import org.example.adventurexp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final PasswordEncoder passwordEncoder;


    public UserDTO getUserDTOById(long id) {
        try {

            User foundUser = userRepository.findById(id)
                    .orElseThrow(() ->
                            new NotFoundException("User not found") // logges i forvejen så id ikke nødvendig
                    );

            return DTOMapper.toDTO(foundUser);
        } catch (DataAccessException e) {
            logger.error("Database problem while retrieving userDTO by id: {}", id, e);
            throw new DatabaseAccessException("A system error occurred. Please try again later.");
        }
    }

    public UserDTO getUserByUsername(String username) {
        try {

            User foundUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> {
                        logger.warn("User with username: {} was not found.", username);
                        return new NotFoundException("User not found");
                    });

            return DTOMapper.toDTO(foundUser);
        } catch (DataAccessException e) {
            logger.error("Database problem while retrieving user by username", e);
            throw new DatabaseAccessException("A system error occurred. Please try again later.");
        }
    }


    public User getUserById(long id) {
        try {

            return userRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("User with id: {} was not found", id);
                        return new NotFoundException("User not found");
                    });
        } catch (DataAccessException e) {
            logger.error("Database problem while retrieving user-entity by id.", e);
            throw new DatabaseAccessException("A system error occurred. Please try again later.");
        }
    }

    public UserDTO signUp(SignUpRequestDTO signUpRequestDTO) {
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


            UserDTO saved = DTOMapper.toDTO(userRepository.save(user));
            logger.info("User with id: {} was signed up successfully", saved.id());
            return saved;

        } catch (DataIntegrityViolationException e) {
            String msg = e.getMostSpecificCause().getMessage().toLowerCase();

            if (msg.contains("duplicate") || msg.contains("unique") || msg.contains("constraint")) {
                if (msg.contains("uc_user_username")) {
                    logger.warn("Username: {} already exists.", signUpRequestDTO.username());
                    throw new DuplicateResourceException("Username already exists");
                } else if (msg.contains("uc_user_email")) {
                    logger.warn("Email: {} already exists.", signUpRequestDTO.email());
                    throw new DuplicateResourceException("Email already exists");
                } else {
                    logger.warn("Database integrity error while saving user.");
                    throw new DuplicateResourceException("The provided information conflicts with an existing user.");
                }
            }
            throw new DatabaseAccessException("A database constraint error occurred while saving user."); // hvis det er noget andet end duplicate så håndter anderledes.
        } catch (DataAccessException e) {
            logger.error("Database access error while saving user.", e);
            throw new DatabaseAccessException("A system error occurred. Please try again later.");
        }
    }

    public UserDTO adminSignUp(AdminSignupDTO adminSignupDTO) {
        try {
            String hashedPassword = passwordEncoder.encode(adminSignupDTO.rawPassword());

            User user = new User(
                    adminSignupDTO.username(),
                    adminSignupDTO.firstName(),
                    adminSignupDTO.lastName(),
                    adminSignupDTO.email()
            );
            user.changeRole(adminSignupDTO.role());
            user.changePasswordHash(hashedPassword);
            UserDTO saved = DTOMapper.toDTO(userRepository.save(user));

            logger.info("Employee with id: {} and role {} was registered successfully", saved.id(), adminSignupDTO.role());


            return saved;
        } catch (DataIntegrityViolationException e) {
            String msg = e.getMostSpecificCause().getMessage().toLowerCase();

            if (msg.contains("duplicate") || msg.contains("unique") || msg.contains("constraint")) {
                if (msg.contains("uc_user_username")) {
                    logger.warn("Username: {} already exists.", adminSignupDTO.username());
                    throw new DuplicateResourceException("Username already exists");
                } else if (msg.contains("uc_user_email")) {
                    logger.warn("Email: {} already exists.", adminSignupDTO.email());
                    throw new DuplicateResourceException("Email already exists");
                } else {
                    logger.warn("Database integrity error while registering user.");
                    throw new DuplicateResourceException("The provided information conflicts with an existing user.");
                }
            }
            throw new DatabaseAccessException("A database constraint error occurred while saving user."); // hvis det er noget andet end duplicate så håndter anderledes.
        } catch (DataAccessException e) {
            logger.error("Database access error while saving user.", e);
            throw new DatabaseAccessException("A system error occurred. Please try again later.");
        }
    }

    public UserDTO updateUser (UserUpdateDTO dto) {

        try {
            User existing = userRepository.findById(dto.id())
                    .orElseThrow(() -> {
                        logger.warn("User with ID: {} was not found", dto.id());
                                return new NotFoundException("User not found");
                    });


            // opdater kun felter der må ændres:
            existing.setUsername(dto.username());
            existing.setFirstName(dto.firstName());
            existing.setLastName(dto.lastName());
            existing.setEmail(dto.email());
            existing.changePasswordHash(passwordEncoder.encode(dto.rawPassword()));
            existing.changeRole(dto.role());

            User updated = userRepository.save(existing);
            logger.info("User with id {} was successfully updated", dto.id());
            return DTOMapper.toDTO(updated);

        } catch (DataIntegrityViolationException e) {
            String msg = e.getMostSpecificCause().getMessage().toLowerCase();

            if (msg.contains("duplicate") || msg.contains("unique") || msg.contains("constraint")) {
                if (msg.contains("uc_user_username")) {
                    logger.warn("Username: {} already exists.", dto.username());
                    throw new DuplicateResourceException("Username already exists");
                } else if (msg.contains("uc_user_email")) {
                    logger.warn("Email: {} already exists.", dto.email());
                    throw new DuplicateResourceException("Email already exists");
                } else {
                    logger.warn("Database integrity error while registering user.");
                    throw new DuplicateResourceException("The provided information conflicts with an existing user.");
                }
            }
            throw new DatabaseAccessException("A database constraint error occurred while updating user."); // hvis det er noget andet end duplicate så håndter anderledes.
        } catch (DataAccessException e) {
            logger.error("Database access error while updating user.", e);
            throw new DatabaseAccessException("A system error occurred. Please try again later.");
        }
    }


    public long deleteUser(long id) {
        try {
            if (!userRepository.existsById(id)) {
                logger.warn("Failed to find user with ID: {}", id);
                throw new NotFoundException("User not found");
            }

            userRepository.deleteById(id);
            logger.info("User with ID: {} deleted successfully", id);
            return id;

        } catch (DataAccessException e) {
            logger.error("Database access error while deleting user", e);
            throw new DatabaseAccessException("A system error occurred. Please try again later.");
        }
    }

    public List<UserDTO> getAllUsers() {
        try {
            return DTOMapper.toUserDTOList(userRepository.findAll());
        } catch (DataAccessException e) {
            logger.error("Data access error while retrieving all users. ", e);
            throw new DatabaseAccessException("A system error occurred. Please try again later.");
        }
    }

}
