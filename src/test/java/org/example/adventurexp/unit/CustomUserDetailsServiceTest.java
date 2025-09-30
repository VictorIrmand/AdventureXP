package org.example.adventurexp.unit;

import org.example.adventurexp.model.Role;
import org.example.adventurexp.model.User;
import org.example.adventurexp.repository.UserRepository;
import org.example.adventurexp.service.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private User mockUser;

    @BeforeEach
    void setup() {
        mockUser = new User(
                1,
                "alicejensen",
                "Alice",
                "Jensen",
                Role.ADMIN,
                "alice@example.com",
                "hashedPassword",
                LocalDateTime.now()
        );
    }

    @Test
    void loadUserByUsername_shouldReturnUser_whenFoundByUsername() {
        // Arrange
        when(userRepository.findByUsername("alicejensen")).thenReturn(Optional.of(mockUser));

        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("alicejensen");

        // Assert
        assertEquals("alicejensen", userDetails.getUsername());
        assertEquals("hashedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));

        verify(userRepository, times(1)).findByUsername("alicejensen");
        verify(userRepository, never()).findByEmail(anyString());
    }

    @Test
    void loadUserByUsername_shouldReturnUser_whenFoundByEmail() {
        // Arrange
        when(userRepository.findByUsername("alice@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("alice@example.com")).thenReturn(Optional.of(mockUser));

        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("alice@example.com");

        // Assert
        assertEquals("alicejensen", userDetails.getUsername()); // stadig username
        assertEquals("hashedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));

        verify(userRepository, times(1)).findByUsername("alice@example.com");
        verify(userRepository, times(1)).findByEmail("alice@example.com");
    }

    @Test
    void loadUserByUsername_shouldThrowException_whenUserNotFound() {
        // Arrange
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("unknown")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("unknown");
        });

        verify(userRepository, times(1)).findByUsername("unknown");
        verify(userRepository, times(1)).findByEmail("unknown");
    }
}
