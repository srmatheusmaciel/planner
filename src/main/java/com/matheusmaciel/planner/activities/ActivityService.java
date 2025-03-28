package com.matheusmaciel.planner.activities;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.matheusmaciel.planner.trip.Trip;
import com.matheusmaciel.planner.trip.TripRepository;


@Service
public class ActivityService {

  @Autowired
  private ActivityRepository repository;


  public ActivityResponse registerActivity(ActivityRequestPayLoad payload, Trip trip) {
    Activity newActivity = new Activity(payload.title(), payload.occurs_at(), trip);

    this.repository.save(newActivity);
    return new ActivityResponse(newActivity.getId());
  }

  public List<ActivityData> getAllActivitiesFromId(UUID tripId){
    return this.repository.findByTripId(tripId).stream()
        .map(activity -> new ActivityData(activity.getId(), activity.getTitle(), activity.getOccursAt()))
        .toList();
}

}
