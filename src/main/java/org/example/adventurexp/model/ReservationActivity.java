package org.example.adventurexp.model;

import jakarta.persistence.*;

@Entity
public class ReservationActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "activity_id")
    private Activity activity;


}
