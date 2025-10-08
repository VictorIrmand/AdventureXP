package org.example.adventurexp.service;

import org.example.adventurexp.dto.ReservationDTO;
import org.example.adventurexp.dto.user.UserDTO;
import org.example.adventurexp.exception.ReservationNotFoundException;
import org.example.adventurexp.mapper.DTOMapper;
import org.example.adventurexp.model.Reservation;
import org.example.adventurexp.model.User;
import org.example.adventurexp.repository.ReservationRepository;
import org.hibernate.action.internal.EntityActionVetoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserService userService;

    @Autowired
    ReservationRepository reservationRepository;

    public ReservationDTO makeReservation(ReservationDTO reservationDTO, UserDTO userDTO){
        User user = userService.getUserById(userDTO.id());
        LocalDateTime startDate = reservationDTO.startDate();

        if (reservationRepository.existsByStartDate(startDate)) {
            throw new IllegalStateException("The selected time slot is already booked.");
        }
        try {
            Reservation reservation = new Reservation(
                    reservationDTO.name(),
                    reservationDTO.participants(),
                    startDate,
                    reservationDTO.isCompanyBooking(),
                    reservationDTO.reservationActivities()
            );
            reservation.setUser(user);

            return DTOMapper.toDTO(reservationRepository.save(reservation));

        } catch (DataIntegrityViolationException e) {
            logger.error("Reservation with name; {} contains database constrains violations",reservationDTO.name());
            throw new IllegalStateException("Database constrain violation; " + e.getMessage());
        }
        catch (DataAccessException e) {
            logger.error("Data access error while saving reservation; {}", reservationDTO.name());
            throw new IllegalStateException("Database error; " + e.getMessage());
        }
    }

    public Reservation getReservationById(int id) {
        try {
            return reservationRepository.findById(id)
                    .orElseThrow(() -> {
                        return new ReservationNotFoundException("Fail to find Reservation!");
                    });
        } catch (DataAccessException e) {
            logger.error("Database problem: {}", e.getMessage());
            throw new IllegalStateException("Database problem: " + e.getMessage());
        }
    }

    public void deleteReservation(int id){
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isPresent()){
        reservationRepository.delete(reservation.get());
        } else {
            throw new ReservationNotFoundException("Reservation with id " + id + " was not found");
        }
    }

    public List<ReservationDTO> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        List<ReservationDTO> dtoList = new ArrayList<>();

        for(Reservation reservation : reservations){
            ReservationDTO dto = DTOMapper.toDTO(reservation);
            dtoList.add(dto);
        }
        return dtoList;




    }
}
