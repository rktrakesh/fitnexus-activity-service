package com.fitnexus.mapper;

import com.fitnexus.dto.response.ActivityResponse;
import com.fitnexus.model.Activity;

public class ActivityDtoMapper {

    public static ActivityResponse mapToDto (Activity activity) {
        return ActivityResponse.builder()
                .id(activity.getId())
                .userId(activity.getUserId())
                .type(activity.getType())
                .caloriesBurned(activity.getCaloriesBurned())
                .duration(activity.getDuration())
                .additionalMatrics(activity.getAdditionalMatrics())
                .createdAt(activity.getCreatedAt())
                .modifiedAt(activity.getModifiedAt())
                .startTime(activity.getStartTime())
                .build();
    }

}
