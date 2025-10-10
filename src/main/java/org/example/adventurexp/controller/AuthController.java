package org.example.adventurexp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.adventurexp.dto.user.LoginRequestDTO;
import org.example.adventurexp.dto.user.SignUpRequestDTO;
import org.example.adventurexp.dto.user.UserDTO;
import org.example.adventurexp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO requestDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDTO.username(),
                            requestDTO.rawPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return ResponseEntity.ok("Login succesfuld");
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Forkert brugernavn eller adgangskode");
        }
    }


    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMe() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();

        UserDTO userDTO = userService.getUserByUsername(name);
        return ResponseEntity.ok().body(userDTO);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpRequestDTO signUpRequestDTO) {
        UserDTO saved = userService.signUp(signUpRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved.username());
    }

}
