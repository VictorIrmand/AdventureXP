package org.example.adventurexp.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.example.adventurexp.dto.activity.ActivityDTO;
import org.example.adventurexp.dto.activity.CreateActivityDTO;
import org.example.adventurexp.dto.activity.UpdateActivityDTO;
import org.example.adventurexp.exception.DatabaseAccessException;
import org.example.adventurexp.exception.DuplicateResourceException;
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
                    .orElseThrow(() -> {
                        logger.warn("Failed to find activity ID: {}", id);
                        return new NotFoundException("Activity not found");
                    });

            return DTOMapper.toDTO(foundActivity);
        } catch (DataAccessException e) {
            logger.error("Database access error while retrieving activity with ID: {}", id, e);
            throw new DatabaseAccessException("A system error occurred. Please try again later.");
        }
    }


    public ActivityDTO createActivity(CreateActivityDTO dto) {

        try {
            Activity activity = DTOMapper.toEntity(dto);
            ActivityDTO saved = DTOMapper.toDTO(activityRepository.save(activity));

            logger.info("Activity with id: {} was successfully created", saved.id());

            return saved;

        } catch (DataIntegrityViolationException e) {
            String msg = e.getMostSpecificCause().getMessage().toLowerCase();

            if (msg.contains("duplicate") || msg.contains("unique") || msg.contains("constraint")) {
                if (msg.contains("uc_activity_name")) {
                    logger.warn("Activity with name '{}' already exists.", dto.name());
                    throw new DuplicateResourceException("Activity name already exists");
                } else {
                    logger.warn("Database integrity error while saving activity '{}'.", dto.name());
                    throw new DuplicateResourceException("The provided information conflicts with an existing activity.");
                }
            }

            logger.error("Unhandled integrity violation while creating activity '{}'.", dto.name());
            throw new DatabaseAccessException("A database constraint error occurred while saving activity.");

        } catch (DataAccessException e) {
            logger.error("Database access error while saving activity '{}': {}", dto.name(), e.getMessage(), e);
            throw new DatabaseAccessException("A system error occurred. Please try again later.");
        }
    }


    public List<ActivityDTO> getAllActivities() {
        try {
            return DTOMapper.toDTOList(activityRepository.findAll());
        } catch (DataAccessException e) {
            logger.error("Data access error while retrieving all activities", e);
            throw new DatabaseAccessException("A system error occurred. Please try again later.");
        }
    }


    public void deleteActivity(long id) {
        try {
            if (!activityRepository.existsById(id)) {
                logger.warn("Failed to find activity with ID: {}", id);
                throw new NotFoundException("Activity not found");
            }
            activityRepository.deleteById(id);
            logger.info("Activity with ID: {} was successfully deleted", id);
        }
        catch (DataAccessException e) {
            logger.error("Data access error while deleting activity with ID: {}", id, e);
            throw new DatabaseAccessException("A system error occurred. Please try again later.");
        }
    }

    public ActivityDTO updateActivity(UpdateActivityDTO dto) {
        try {
            Activity existing = activityRepository.findById(dto.id())
                    .orElseThrow(() -> {
                        logger.warn("Activity ID: {} was not found.", dto.id());
                        return new NotFoundException("Activity not found");
                    });

            // Opdater kun felter der må ændres
            existing.setName(dto.name());
            existing.setDescription(dto.description());
            existing.setAgeLimit(dto.ageLimit());
            existing.setMaxParticipants(dto.maxParticipants());
            existing.setMinParticipants(dto.minParticipants());
            existing.setPricePerMinutePerPerson(dto.pricePerMinutePerPerson());
            existing.setImgUrl(dto.imgUrl());


            Activity updated = activityRepository.save(existing);
            logger.info("Activity with id {} was successfully updated", dto.id());
            return DTOMapper.toDTO(updated);

        } catch (DataIntegrityViolationException e) {
            String msg = e.getMostSpecificCause().getMessage().toLowerCase();

            if (msg.contains("duplicate") || msg.contains("unique") || msg.contains("constraint")) {
                if (msg.contains("uc_activity_name")) {
                    logger.warn("Activity with name '{}' already exists.", dto.name());
                    throw new DuplicateResourceException("Activity name already exists");
                } else {
                    logger.warn("Database integrity error while updating activity '{}'.", dto.name());
                    throw new DuplicateResourceException("The provided information conflicts with an existing activity.");
                }
            }
            logger.error("Unhandled integrity violation while updating activity '{}'.", dto.name());
            throw new DatabaseAccessException("A database constraint error occurred while saving activity.");

        } catch (DataAccessException e) {
            logger.error("Database access error while updating activity {}", dto.name(), e);
            throw new DatabaseAccessException("A system error occurred. Please try again later.");
        }
    }
}
