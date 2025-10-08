package org.example.adventurexp.controller;

import lombok.RequiredArgsConstructor;
import org.example.adventurexp.dto.activity.ActivityDTO;
import org.example.adventurexp.service.ActivityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/activities")
public class ActivityController {

    private final static Logger logger = LoggerFactory.getLogger(ActivityController.class);
    private final ActivityService activityService;




    @GetMapping
    public ResponseEntity<List<ActivityDTO>> getAllActivities() {

        List<ActivityDTO> activityDTOList = activityService.getAllActivities();

        return ResponseEntity.ok(activityDTOList);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ActivityDTO> getActivityById(@PathVariable long id) {
        ActivityDTO activityDTO = activityService.getActivityDTOById(id);
        return ResponseEntity.ok(activityDTO);
    }






}
