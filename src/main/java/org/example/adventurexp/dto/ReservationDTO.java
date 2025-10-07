package org.example.adventurexp.dto;

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
         @NotBlank
         String startDate,
         @Min(1)
         int participants,
         boolean isCompanyBooking,
         @NotNull
         List<ReservationActivity> reservationActivities
) {
}
