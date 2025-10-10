package org.example.adventurexp.controller;

import lombok.RequiredArgsConstructor;
import org.example.adventurexp.dto.activity.ActivityDTO;
import org.example.adventurexp.dto.user.UserDTO;
import org.example.adventurexp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable long id) {
        UserDTO foundUserDTO = userService.getUserDTOById(id);
        return ResponseEntity.ok().body(foundUserDTO);
    }

}
