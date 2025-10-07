package org.example.adventurexp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.adventurexp.dto.activity.ActivityDTO;
import org.example.adventurexp.dto.activity.CreateActivityDTO;
import org.example.adventurexp.dto.user.AdminRegisterSignUpDTO;
import org.example.adventurexp.service.ActivityService;
import org.example.adventurexp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin")
public class AdminController {

    private final UserService userService;
    private final ActivityService activityService;

    @PostMapping("/register")
    public ResponseEntity<?> registerEmployee(@Valid @RequestBody AdminRegisterSignUpDTO adminRegisterSignUpDTO){
        userService.adminSignUp(adminRegisterSignUpDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PostMapping("/create-activity")
    public ResponseEntity<ActivityDTO> createActivity (@Valid @RequestBody CreateActivityDTO dto) {
       ActivityDTO saved = activityService.createActivity(dto);
       return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

}
