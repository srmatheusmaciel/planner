package com.matheusmaciel.planner.activities;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matheusmaciel.planner.participant.Participant;

public interface ActivityRepository extends JpaRepository<Activity, UUID> {
  List<Activity> findByTripId(UUID tripId);
}
