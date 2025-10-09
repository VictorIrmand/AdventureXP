package org.example.adventurexp.unit;

import org.example.adventurexp.dto.activity.ActivityDTO;
import org.example.adventurexp.dto.activity.CreateActivityDTO;
import org.example.adventurexp.model.Activity;
import org.example.adventurexp.repository.ActivityRepository;
import org.example.adventurexp.service.ActivityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ActivityTest {
    private List<Activity> mockActivities;

    private List<ActivityDTO> mockActivityDTOList;


    @InjectMocks
    private ActivityService activityService;

    @Mock
    private ActivityRepository activityRepository;


    @BeforeEach
    void setup() {
        mockActivities = new ArrayList<>();

        mockActivities.add(new Activity(1, "paintball", "description...", 18, 2, 3, 3, "imageUrl"));
        mockActivities.add(new Activity(2, "gokart", "description...", 16, 4, 8, 2, "imageUrl"));
        mockActivities.add(new Activity(3, "sumo-wrestling", "description...", 14, 5, 2, 3, "imageUrl"));


        mockActivityDTOList = new ArrayList<>();
        mockActivityDTOList.add(new ActivityDTO(1, "paintball", "description...", 18, 2, 3, 3, "imageUrl"));
        mockActivityDTOList.add(new ActivityDTO(2, "gokart", "description...", 16, 4, 8, 2, "imageUrl"));
        mockActivityDTOList.add(new ActivityDTO(3, "sumo-wrestling", "description...", 14, 5, 2, 3, "imageUrl"));

    }

    @Test
    void getActivityDTOById_shouldReturnActivityDTO_whenActivityExists() {

        //arrange
        Activity paintball = mockActivities.getFirst();

        ActivityDTO paintballDTO = mockActivityDTOList.getFirst();

        // mock behavior
        Mockito.when(activityRepository.findById(1L)).thenReturn(Optional.of(paintball));

        //act
        ActivityDTO result = activityService.getActivityDTOById(1);

        // assert
        assertEquals(result, paintballDTO);

        // verify
        Mockito.verify(activityRepository, Mockito.times(1)).findById(1L);

    }

    @Test
    void createActivity_savesActivity_whenActivityIsValid() {
        // Arrange
        ActivityDTO expectedDTO = mockActivityDTOList.getLast();

        CreateActivityDTO createActivityDTO = new CreateActivityDTO("sumo-wrestling", "description...", 14, 5, 2, 3, "imageUrl");

        Activity savedEntity = mockActivities.getLast();

        // mock repository til at returner en activity
        Mockito.when(activityRepository.save(any())).thenReturn(savedEntity);

        // act
        ActivityDTO saved = activityService.createActivity(createActivityDTO);

        // assert
        assertEquals(expectedDTO, saved);

        // Verify at save blev kaldt
        Mockito.verify(activityRepository, Mockito.times(1)).save(any());
    }


    @Test
    void deleteActivity_deletesActivity_whenActivityExists() {

        // arrange
        List<Activity> activityList = mockActivities;

        Activity paintball = activityList.getFirst();


        // mock
        Mockito.doAnswer(invocation -> {
            activityList.remove(paintball);
            return null;
        }).when(activityRepository).deleteById(1L);


        // act
        activityService.deleteActivity(1);

        // assert
        assertNotEquals(activityList.getFirst().getId(), paintball.getId());

        // verify

        Mockito.verify(activityRepository, Mockito.times(1)).deleteById(any());
    }


    @Test
    void getAllActivities_shouldReturnActivityDTOList () {
       // arrange
        List<Activity> activityList = mockActivities;


        List<ActivityDTO> expectedDTOList = mockActivityDTOList;

        // mock
        Mockito.when(activityRepository.findAll()).thenReturn(activityList);

        // act
        List<ActivityDTO> foundActivityDTOList = activityService.getAllActivities();


        // assert
        for (int i = 0; i < expectedDTOList.size(); i++) {
            assertEquals(expectedDTOList.get(i), foundActivityDTOList.get(i));
        }

        //
        Mockito.verify(activityRepository, Mockito.times(1)).findAll();

        assertNotSame(mockActivityDTOList, foundActivityDTOList);
    }





}
