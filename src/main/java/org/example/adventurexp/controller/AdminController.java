package org.example.adventurexp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.adventurexp.dto.activity.ActivityDTO;
import org.example.adventurexp.dto.activity.CreateActivityDTO;
import org.example.adventurexp.dto.activity.UpdateActivityDTO;
import org.example.adventurexp.dto.user.AdminSignupDTO;
import org.example.adventurexp.dto.user.UserDTO;
import org.example.adventurexp.dto.user.UserUpdateDTO;
import org.example.adventurexp.service.ActivityService;
import org.example.adventurexp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;
    private final ActivityService activityService;

    @PostMapping("/register-employee")
    public ResponseEntity<String> registerEmployee(@Valid @RequestBody AdminSignupDTO adminSignupDTO) {
        UserDTO saved = userService.adminSignUp(adminSignupDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved.username());
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<Long> deleteUser(@RequestParam long id) {
        long savedId = userService.deleteUser(id);
        return ResponseEntity.ok().body(savedId);
    }

    @PutMapping("/update-user")
    public ResponseEntity<String> updateUser (@Valid @RequestBody UserUpdateDTO userUpdateDTO) {

        UserDTO updatedUser = userService.updateUser(userUpdateDTO);

        return ResponseEntity.ok().body(updatedUser.username());
    }


    @PostMapping("/create-activity")
    public ResponseEntity<ActivityDTO> createActivity(@Valid @RequestBody CreateActivityDTO dto) {
        ActivityDTO saved = activityService.createActivity(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/delete-activity")
    public ResponseEntity<?> deleteActivity(@RequestParam long id) {

        activityService.deleteActivity(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update-activity")
    public ResponseEntity<ActivityDTO> updateActivity(@Valid @RequestBody UpdateActivityDTO dto) {


        ActivityDTO updated = activityService.updateActivity(dto);


        return ResponseEntity.ok().body(updated);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {

        List<UserDTO> userDTOList = userService.getAllUsers();

        return ResponseEntity.ok(userDTOList);
    }

}
