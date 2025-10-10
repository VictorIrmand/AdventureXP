package org.example.adventurexp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.adventurexp.model.ReservationActivity;
import org.example.adventurexp.model.User;

import java.time.LocalDateTime;
import java.util.List;

public record ReservationDTO(
        @NotBlank
        @Size(max = 100)
        String name,

        @NotNull(message = "Date and Time cannot be null.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime startDate,

        @Min(value = 1, message = "Participants must be over 1.")
        @NotNull
        int participants,

        boolean isCompanyBooking,
        @NotNull
        List<ReservationActivity> reservationActivities
) {
}
