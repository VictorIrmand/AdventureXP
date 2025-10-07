package org.example.adventurexp.repository;

import org.example.adventurexp.model.ReservationActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationActivityRepository extends JpaRepository<ReservationActivity,Integer> {
}
