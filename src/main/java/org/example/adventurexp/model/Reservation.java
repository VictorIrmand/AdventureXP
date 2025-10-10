package org.example.adventurexp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne
    @Setter
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime startDate;

    private int participants;

    private boolean isCompanyBooking;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservationActivity> reservationActivities = new ArrayList<>();

    public Reservation(String name, int participants, LocalDateTime startDate, boolean isCompanyBooking, List<ReservationActivity> reservationActivities) {
        this.name = name;
        this.participants = participants;
        this.isCompanyBooking = isCompanyBooking;
        this.startDate = startDate;
        this.reservationActivities = reservationActivities;
    }

}
