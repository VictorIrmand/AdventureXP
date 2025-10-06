package org.example.adventurexp.unit;

import org.example.adventurexp.dto.AdminRegisterSignUpDTO;
import org.example.adventurexp.dto.SignUpRequestDTO;
import org.example.adventurexp.dto.UserDTO;
import org.example.adventurexp.model.Role;
import org.example.adventurexp.model.User;

import org.example.adventurexp.repository.UserRepository;
import org.example.adventurexp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserTest {
    private List<User> mockUsers;
    private LocalDateTime createdTime;
    private List<UserDTO> mockUsersDTOList;

    @Mock
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;




    @BeforeEach
    void setup() {
        createdTime = LocalDateTime.of(2024, 1, 1, 12, 0);
        // Det her er entiteter
        mockUsers = new ArrayList<>();
        mockUsers.add(new User(1, "alicejensen", "alice", "Jensen", Role.ACTIVITY_STAFF, "Martin@gmail.com", "123", createdTime));
        mockUsers.add(new User(2, "bobDylan", "bob", "Dylan", Role.ADMIN, "Dylan@hotmail.dk", "123", createdTime));
        mockUsers.add(new User(3, "charlieNielsen", "charlie", "Nielsen", Role.RESERVATION_STAFF, "Nielsen@Gmail.com", "123", createdTime));

        // Det her er DTO'er
        mockUsersDTOList = new ArrayList<>();
        mockUsersDTOList.add(new UserDTO(1, "alicejensen", "alice", "Jensen", Role.ACTIVITY_STAFF, "Martin@gmail.com", createdTime));
        mockUsersDTOList.add(new UserDTO(2, "bobDylan", "bob", "Dylan", Role.ADMIN, "Dylan@hotmail.dk", createdTime));
        mockUsersDTOList.add(new UserDTO(3, "charlieNielsen", "charlie", "Nielsen", Role.RESERVATION_STAFF, "Nielsen@Gmail.com", createdTime));

    }

    @Test
    void getUserById_shouldReturnUser_whenUserExists() {
        // Arrange
        User bob = mockUsers.get(1);

        //Mock behaviour
        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.of(bob));

        // ACT
        User result = userService.getUserById(2);

        //ASSERT
        assertEquals(bob, result);

        // Verify
        Mockito.verify(userRepository, Mockito.times(1)).findById(2L);

    }

    @Test
    void getUserByUsername_shouldReturnUser_whenUserExists() {
        // Arrange
        UserDTO bobDTO = mockUsersDTOList.get(1);
        User bob = mockUsers.get(1);

        //Mock behaviour
        Mockito.when(userRepository.findByUsername("bobDylan")).thenReturn(Optional.of(bob));

        // ACT
        UserDTO result = userService.getUserByUsername("bobDylan");

        //ASSERT
        assertEquals(bobDTO, result);

        // Verify
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername("bobDylan");

    }

    @Test
    void signUp_savesUser_whenUserIsValid() {
        // Arrange
        UserDTO expectedDTO = new UserDTO(
                4, "Karstengammel", "Karsten", "Gammel",
                Role.CUSTOMER, "karstengammel@email.com",
                createdTime
        );

        SignUpRequestDTO userSignUp = new SignUpRequestDTO(
                "KarstenGammel", "Karsten", "Gammel",
                "karstengammel@email.com",
                "123"
        );

        User savedEntity = new User(
                4, "Karstengammel", "Karsten", "Gammel",
                Role.CUSTOMER, "karstengammel@email.com",
                "123", // vi ignorerer encoding i denne simple test
                createdTime
        );

        // Mock repository til at returnere en User
        Mockito.when(userRepository.save(any())).thenReturn(savedEntity);

        // Act
        UserDTO saved = userService.signUp(userSignUp);

        // Assert – vi tjekker bare at DTO'en matcher
        assertEquals(expectedDTO, saved);

        // Verify at save blev kaldt
        Mockito.verify(userRepository, Mockito.times(1)).save(any());
    }

    @Test
    void adminSignUp_SavesEmployeeWithRole_WhenUserIsValid() {
        //Arrange
        UserDTO extectedDTO = new UserDTO(
                6, "boJensen", "Bo", "Jensen",
                Role.RESERVATION_STAFF, "bojensen@gmail.com",
                createdTime
        );
        AdminRegisterSignUpDTO userSignUp = new AdminRegisterSignUpDTO(
                "boJensen", "Bo", "Jensen",
                "bojensen@gmail.com", Role.RESERVATION_STAFF,
                "123"
        );

        User savedEntity = new User (
                6, "boJensen", "Bo", "Jensen",
                Role.RESERVATION_STAFF,
                "bojensen@gmail.com", "123",
                createdTime
        );

        // Mock repository til at retunere en User
        Mockito.when(userRepository.save(any())).thenReturn(savedEntity);

        // ACT
        UserDTO saved = userService.adminSignUp(userSignUp);

        // Assert - Tjekker om DTO'en findes og har den rigtige rolle
        assertEquals(extectedDTO,saved);
        assertEquals(Role.RESERVATION_STAFF,saved.role());

        // Verify at save blev kaldt
        Mockito.verify(userRepository, Mockito.times(1)).save(any());


    }


}


