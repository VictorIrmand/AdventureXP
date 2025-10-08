package org.example.adventurexp.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.example.adventurexp.dto.activity.ActivityDTO;
import org.example.adventurexp.dto.activity.CreateActivityDTO;
import org.example.adventurexp.dto.activity.UpdateActivityDTO;
import org.example.adventurexp.exception.NotFoundException;
import org.example.adventurexp.mapper.DTOMapper;
import org.example.adventurexp.model.Activity;
import org.example.adventurexp.repository.ActivityRepository;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private static final Logger logger = LoggerFactory.getLogger(ActivityService.class);
    private final ActivityRepository activityRepository;


    public ActivityDTO getActivityDTOById(long id) {
        try {

            Activity foundActivity = activityRepository.findById(id)
                    .orElseThrow(() ->
                            new NotFoundException("Failed to find activity with id: " + id)
                    );

            return DTOMapper.toDTO(foundActivity);

        } catch (DataAccessException e) {
            logger.error("Database problem: {}", e.getMessage());
            throw new IllegalStateException("Database problem: " + e.getMessage());
        }
    }


    public ActivityDTO createActivity(CreateActivityDTO dto) {

        try {
            Activity activity = DTOMapper.toEntity(dto);
            ActivityDTO saved = DTOMapper.toDTO(activityRepository.save(activity));

            logger.info("Activity with id: {} was successfully created", saved.id());

            return saved;

        } catch (DataIntegrityViolationException e) {
            logger.error("Activity with name; {} contains database constrains violations", dto.name());
            throw new IllegalStateException("Database constrain violation; " + e.getMessage());
        } catch (DataAccessException e) {
            logger.error("Data access error while saving activity: {}", dto.name());
            throw new IllegalStateException("Database error; " + e.getMessage());
        }
    }

    public List<ActivityDTO> getAllActivities() {
        try {
            return DTOMapper.toDTOList(activityRepository.findAll());
        } catch (DataAccessException e) {
            logger.error("Data access error while retrieving all activities: {}", e.getMessage());
            throw new IllegalStateException("Database error; " + e.getMessage());
        }
    }


    public void deleteActivity(long id) {
        try {
            activityRepository.deleteById(id);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid id: {}", e.getMessage());
            throw new IllegalStateException("Invalid id");
        } catch (DataAccessException e) {
            logger.error("Data access error while retrieving all activities: {}", e.getMessage());
            throw new IllegalStateException("Database error; " + e.getMessage());
        }
    }

    public ActivityDTO updateActivity(UpdateActivityDTO dto) {
        try {
            Activity existing = activityRepository.findById(dto.id())
                    .orElseThrow(() -> new IllegalStateException("Activity not found"));

            // Opdater kun felter der må ændres
            existing.setName(dto.name());
            existing.setDescription(dto.description());
            existing.setAgeLimit(dto.ageLimit());
            existing.setMaxParticipants(dto.maxParticipants());
            existing.setMinParticipants(dto.minParticipants());
            existing.setPricePerMinutePerPerson(dto.pricePerMinutePerPerson());
            existing.setImgUrl(dto.imgUrl());


            Activity updated = activityRepository.save(existing);
            logger.info("Activity with id {} successfully updated", dto.id());
            return DTOMapper.toDTO(updated);

        } catch (DataIntegrityViolationException e) {
            logger.error("Activity {} violates DB constraints", dto.name(), e);
            throw new IllegalStateException("Database constraint violation: " + e.getMostSpecificCause().getMessage());
        } catch (DataAccessException e) {
            logger.error("Database access error while updating activity {}", dto.name(), e);
            throw new IllegalStateException("Database error: " + e.getMessage());
        }
    }
}
