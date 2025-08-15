package com.fitnexus.service.impl;

import com.fitnexus.dto.request.ActivityRequest;
import com.fitnexus.dto.response.ActivityResponse;
import com.fitnexus.mapper.ActivityDtoMapper;
import com.fitnexus.model.Activity;
import com.fitnexus.repository.ActivityRepository;
import com.fitnexus.service.ActivityService;
import com.fitnexus.service.UserValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final UserValidationService validationService;
    private final RabbitTemplate template;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;
    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    @Override
    public ActivityResponse addActivities(ActivityRequest activityRequest) {
        try {
            if (activityRequest != null) {
                boolean userValidation = validationService.userValidation(activityRequest.getUserId());
                if (!userValidation) {
                    throw new RuntimeException("User not found for userId: " + activityRequest.getUserId());
                }
                Activity activity = Activity.builder()
                        .userId(activityRequest.getUserId())
                        .type(activityRequest.getType())
                        .caloriesBurned(activityRequest.getCaloriesBurned())
                        .duration(activityRequest.getDuration())
                        .startTime(activityRequest.getStartTime())
                        .additionalMatrics(activityRequest.getAdditionalMatrics())
                        .build();

                Activity savedActivity = activityRepository.save(activity);

                // publishing the saves activity in rabbitMQ
                template.convertAndSend(exchange, routingKey, savedActivity);

                return ActivityDtoMapper.mapToDto(savedActivity);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Exception while adding new activities: " + e.getMessage());
        }
    }

    @Override
    public List<ActivityResponse> getActivitiesByUserId(String userId) {
        try {
            List <Activity> activities = activityRepository.findByUserId(userId);
            if (activities != null) {
                return activities.stream()
                        .map(ActivityDtoMapper::mapToDto)
                        .toList();
            }
            throw new RuntimeException("There are no activities found for userId: " + userId);
        } catch (Exception e) {
            throw new RuntimeException("Exception while getting activities for userId: " + userId);
        }
    }

    @Override
    public ActivityResponse getActivitiesById(String id) {
        try {
            return activityRepository.findById(id)
                    .map(ActivityDtoMapper::mapToDto)
                    .orElseThrow(() -> new RuntimeException("There are no activities found for Id: " + id));
        } catch (Exception e) {
            throw new RuntimeException("Exception while getting activities for id: " + id);
        }
    }

}
