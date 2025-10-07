package org.example.adventurexp.service;

import lombok.RequiredArgsConstructor;
import org.example.adventurexp.dto.activity.ActivityDTO;
import org.example.adventurexp.dto.activity.CreateActivityDTO;
import org.example.adventurexp.exception.NotFoundException;
import org.example.adventurexp.mapper.DTOMapper;
import org.example.adventurexp.model.Activity;
import org.example.adventurexp.repository.ActivityRepository;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

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

}
