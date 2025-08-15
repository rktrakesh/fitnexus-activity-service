package com.fitnexus.service;

import com.fitnexus.dto.request.ActivityRequest;
import com.fitnexus.dto.response.ActivityResponse;

import java.util.List;

public interface ActivityService {

    ActivityResponse addActivities(ActivityRequest activityRequest);

    List<ActivityResponse> getActivitiesByUserId(String userId);

    ActivityResponse getActivitiesById(String id);
}
