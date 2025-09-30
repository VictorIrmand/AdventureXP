package org.example.adventurexp.controller;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.example.adventurexp.dto.LoginRequestDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthController authController;

    @Test
    void login_shouldReturnOk_whenCredentialsAreCorrect() {
        LoginRequestDTO request = new LoginRequestDTO("alice", "password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("alice","password"));

        ResponseEntity<?> response = authController.login(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Login succesfuld", response.getBody());
    }

    @Test
    void login_shouldReturnUnauthorized_whenCredentialsAreWrong() {
        LoginRequestDTO request = new LoginRequestDTO("alice", "wrong");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        ResponseEntity<?> response = authController.login(request);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Forkert brugernavn/email eller password", response.getBody());
    }
}
