package com.matheusmaciel.planner.activities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import com.matheusmaciel.planner.trip.Trip;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "activities")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Activity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(name= "title", nullable = false)
  private String title;

  @Column(name= "occurs_at", nullable = false)
  private LocalDateTime occursAt;

  @ManyToOne
  @JoinColumn(name = "trip_id")
  private Trip trip;

  public Activity(String title, String occursAt, Trip trip) {
    this.title = title;
    this.occursAt = LocalDateTime.parse(occursAt, DateTimeFormatter.ISO_DATE_TIME);
    this.trip = trip;
  }

}
