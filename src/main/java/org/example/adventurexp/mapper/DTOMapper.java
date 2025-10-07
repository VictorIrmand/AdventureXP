package org.example.adventurexp.mapper;

import org.example.adventurexp.dto.ReservationDTO;
import org.example.adventurexp.dto.UserDTO;
import org.example.adventurexp.model.Reservation;
import org.example.adventurexp.model.User;

import java.time.format.DateTimeFormatter;

public class DTOMapper {

    // user

    public static UserDTO toDTO (User user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = user.getCreatedAt() != null
                ? user.getCreatedAt().format(formatter)
                : null;

        return new UserDTO (
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.getEmail(),
                formattedDate
        );
    }

    public static ReservationDTO toDTO (Reservation reservation) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return new ReservationDTO(
                reservation.getName(),
                reservation.getStartDate().format(formatter),
                reservation.getParticipants(),
                reservation.isCompanyBooking(),
                reservation.getReservationActivities()
        );
    }

}
