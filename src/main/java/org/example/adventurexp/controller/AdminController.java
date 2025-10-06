package org.example.adventurexp.controller;

import lombok.RequiredArgsConstructor;
import org.example.adventurexp.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;



}
