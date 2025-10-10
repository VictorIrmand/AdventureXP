package org.example.adventurexp.service;

import lombok.RequiredArgsConstructor;
import org.example.adventurexp.dto.ReservationDTO;
import org.example.adventurexp.dto.user.UserDTO;
import org.example.adventurexp.exception.DatabaseAccessException;
import org.example.adventurexp.exception.DuplicateResourceException;
import org.example.adventurexp.exception.NotFoundException;
import org.example.adventurexp.mapper.DTOMapper;
import org.example.adventurexp.model.Reservation;
import org.example.adventurexp.model.User;
import org.example.adventurexp.repository.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserService userService;
    private final ReservationRepository reservationRepository;

    public ReservationDTO makeReservation(ReservationDTO reservationDTO, UserDTO userDTO) {
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

            String msg = e.getMostSpecificCause().getMessage().toLowerCase();
            if (msg.contains("duplicate") || msg.contains("unique") || msg.contains("constraint")) {
                if (msg.contains("uc_reservation_name")) {
                    logger.warn("Reservation with name '{}' already exists.", reservationDTO.name());
                    throw new DuplicateResourceException("Reservation name already exists");
                }
            }
            logger.error("Reservation with name; {} contains database constrains violations", reservationDTO.name());
            throw new DuplicateResourceException("The provided information conflicts with an existing reservation.");
        } catch (DataAccessException e) {
            logger.error("Data access error while saving reservation; {}", reservationDTO.name());
            throw new DatabaseAccessException("A system error occurred. Please try again later.");
        }
    }

    public Reservation getReservationById(int id) {
        try {
            return reservationRepository.findById(id)
                    .orElseThrow(() -> {
                        return new NotFoundException("Reservation not found.");
                    });
        } catch (DataAccessException e) {
            logger.error("Database problem while retrieving reservation by id: {}", id, e);
            throw new DatabaseAccessException("A system error occurred. Please try again later.");
        }
    }

    public void deleteReservation(int id) {
        try {
            Optional<Reservation> reservation = reservationRepository.findById(id);
            if (reservation.isPresent()) {
                reservationRepository.delete(reservation.get());
            } else {
                throw new NotFoundException("Reservation not found.");
            }
        } catch (DataAccessException e) {
            logger.error("Data access error while deleting reservation with ID: {}", id, e);
            throw new DatabaseAccessException("A system error occurred. Please try again later.");
        }
    }

    public List<ReservationDTO> getAllReservations() {
        try {
            List<Reservation> reservations = reservationRepository.findAll();
            List<ReservationDTO> dtoList = new ArrayList<>();

            for (Reservation reservation : reservations) {
                ReservationDTO dto = DTOMapper.toDTO(reservation);
                dtoList.add(dto);
            }
            return dtoList;
        } catch (DataAccessException e) {
            logger.error("Failed to retrieve reservations from database.", e);
            throw new DatabaseAccessException("A system error occurred. Please try again later.");
        }

    }

    public List<ReservationDTO> getReservationsByUserId(long userId) {
        try {
            List<Reservation> reservations = reservationRepository.findAll();
            List<ReservationDTO> dtoList = new ArrayList<>();

            for (Reservation reservation : reservations) {
                if (reservation.getUser() != null && reservation.getUser().getId() == userId) {
                    dtoList.add(DTOMapper.toDTO(reservation));
                }
            }
            return dtoList;
            } catch (DataAccessException e) {
            logger.error("Failed to retrieve reservations from database.", e);
            throw new DatabaseAccessException("A system error occurred. Please try again later.");
        }



    }

}
