package org.example.adventurexp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotBlank
    private int ageLimit;
    @NotBlank
    private double price;
    @NotBlank
   private int participants; // Valideret i DTO.
}
