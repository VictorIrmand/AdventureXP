package org.example.adventurexp.mapper;

import org.example.adventurexp.dto.ReservationDTO;
import org.example.adventurexp.model.Reservation;
import org.example.adventurexp.dto.activity.ActivityDTO;
import org.example.adventurexp.dto.activity.CreateActivityDTO;
import org.example.adventurexp.dto.user.UserDTO;
import org.example.adventurexp.model.Activity;
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



    // activity
    public static Activity toEntity (CreateActivityDTO createActivityDTO) {
        return new Activity(
                createActivityDTO.name(),
                createActivityDTO.description(),
                createActivityDTO.ageLimit(),
                createActivityDTO.pricePerMinutePerPerson(),
                createActivityDTO.maxParticipants(),
                createActivityDTO.minParticipants()
        );
    }

    public static ActivityDTO toDTO (Activity activity) {

        return new ActivityDTO(
                activity.getId(),
                activity.getName(),
                activity.getDescription(),
                activity.getAgeLimit(),
                activity.getPricePerMinutePerPerson(),
                activity.getMaxParticipants(),
                activity.getMinParticipants()
        );
    }
    public static ReservationDTO toDTO (Reservation reservation) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return new ReservationDTO(
                reservation.getName(),
                reservation.getStartDate(),
                reservation.getParticipants(),
                reservation.isCompanyBooking(),
                reservation.getReservationActivities()
        );
    }

}
