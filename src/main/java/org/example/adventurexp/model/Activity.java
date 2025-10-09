package org.example.adventurexp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "activities")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Lob
    private String description;

    @Column(nullable = false)
    private int ageLimit;

    @Column(nullable = false)
    private double pricePerMinutePerPerson;

    @Column(nullable = false)
    private int maxParticipants;

    @Column(nullable = false)
    private int minParticipants;

    private String imgUrl;

    public Activity(String name, String description, int ageLimit, double pricePerMinutePerPerson, int maxParticipants, int minParticipants, String imgUrl) {
        this.name = name;
        this.description = description;
        this.ageLimit = ageLimit;
        this.pricePerMinutePerPerson = pricePerMinutePerPerson;
        this.maxParticipants = maxParticipants;
        this.minParticipants = minParticipants;
        this.imgUrl = imgUrl;
    }




}
