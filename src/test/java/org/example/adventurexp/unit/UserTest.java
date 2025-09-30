package org.example.adventurexp.unit;

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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
    public class UserServiceTest {
        private List<User> mockUsers;
        private LocalDateTime createdTime;

        @Mock
        private UserRepository userRepository;

        @InjectMocks
        private UserService userService;


        @BeforeEach
        void setup() {
            createdTime = LocalDateTime.of(2024, 1, 1, 12, 0);

            mockUsers = new ArrayList<>();
            mockUsers.add(new User(1, "alice", "Jensen", Role.ACTIVITY_STAFF, "Martin@gmail.com", "123", createdTime));
            mockUsers.add(new User(2, "bob", "Dylan", Role.ADMIN, "Dylan@hotmail.dk", "123", createdTime));
            mockUsers.add(new User(3, "charlie", "Nielsen", Role.RESERVATION_STAFF, "Nielsen@Gmail.com", "123", createdTime));
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
            assertEquals(bob,result);

            // Verify
            Mockito.verify(userRepository, Mockito.times(1)).findById(2L);

        }

    }

