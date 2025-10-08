package org.example.adventurexp.controller;

import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.example.adventurexp.dto.ReservationDTO;
import org.example.adventurexp.dto.user.UserDTO;
import org.example.adventurexp.dto.user.UserDTO;
import org.example.adventurexp.model.User;
import org.example.adventurexp.service.ReservationService;
import org.example.adventurexp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/user")
public class ReservationController {

    @Autowired
    ReservationService reservationService;

    @Autowired
    UserService userService;

    @PostMapping("/reservation")
    public ResponseEntity<?> addReservation(@Valid @RequestBody ReservationDTO reservationDTO, Authentication authentication){
        org.example.adventurexp.dto.user.UserDTO user = userService.getUserByUsername(authentication.getName());
        reservationService.makeReservation(reservationDTO, user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/reservation/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable int id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



}
