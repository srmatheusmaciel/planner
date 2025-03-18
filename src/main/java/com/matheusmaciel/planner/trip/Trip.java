package com.matheusmaciel.planner.trip;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
//@Table(name = "trips")
public class Trip {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String destination;

  @Column(name= "starts_at", nullable = false)
  private LocalDateTime startsAt;

  @Column(name= "ends_at", nullable = false)
  private LocalDateTime endsAt;

  @Column(name= "is_confirmed", nullable = false)
  private Boolean isConfirmed;

  @Column(name= "owner_name", nullable = false)
  private String ownerName;

  @Column(name= "owner_email", nullable = false)
  private String ownerEmail;
}
