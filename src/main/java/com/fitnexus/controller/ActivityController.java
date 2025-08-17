package com.fitnexus.controller;

import com.fitnexus.dto.request.ActivityRequest;
import com.fitnexus.dto.response.ActivityResponse;
import com.fitnexus.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @PostMapping("/addActivities")
    public ResponseEntity<ActivityResponse> addNewActivity (@RequestBody ActivityRequest activityRequest, @RequestHeader ("X-USER-ID") String userId) {
        if (userId != null) {
            activityRequest.setUserId(userId);
        }
        return ResponseEntity.ok(activityService.addActivities(activityRequest));
    }

    @GetMapping("/getActivitiesByUserId")
    public ResponseEntity<List<ActivityResponse>> getActivitiesByUserId (@RequestHeader ("X-USER-ID") String userId) {
        return ResponseEntity.ok(activityService.getActivitiesByUserId(userId));
    }

    @GetMapping("/getActivitiesById/{id}")
    public ResponseEntity<ActivityResponse> addNewActivity (@PathVariable String id) {
        return ResponseEntity.ok(activityService.getActivitiesById(id));
    }

}
