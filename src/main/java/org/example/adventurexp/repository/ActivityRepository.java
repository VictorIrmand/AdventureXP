package org.example.adventurexp.repository;

import org.example.adventurexp.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> id(long id);


    boolean existsByid(long id);
}
