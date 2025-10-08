package org.example.adventurexp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.adventurexp.dto.activity.ActivityDTO;
import org.example.adventurexp.dto.activity.CreateActivityDTO;
import org.example.adventurexp.dto.activity.UpdateActivityDTO;
import org.example.adventurexp.dto.user.AdminRegisterSignUpDTO;
import org.example.adventurexp.service.ActivityService;
import org.example.adventurexp.service.ReservationService;
import org.example.adventurexp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin")
public class AdminController {

    private final UserService userService;
    private final ActivityService activityService;
    private final ReservationService reservationService;

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

    @DeleteMapping("/delete-activity")
    public ResponseEntity<?> deleteActivity(@RequestParam long id) {

        activityService.deleteActivity(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update-activity")
    public ResponseEntity<ActivityDTO> updateActivity (@Valid @RequestBody UpdateActivityDTO dto) {


        ActivityDTO updated = activityService.updateActivity(dto);

        return ResponseEntity.ok().body(updated);
    }

}
