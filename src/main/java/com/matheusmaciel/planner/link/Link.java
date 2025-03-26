package com.matheusmaciel.planner.link;

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
@Table(name="links")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Link {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "title", nullable=false)
  private String title;

  @Column(name = "url", nullable=false)
  private String url;

  @ManyToOne
  @JoinColumn(name = "trip_id", nullable = false)
  private Trip trip;

  public Link(String title, String url, Trip trip) {
    this.title = title;
    this.url = url;
    this.trip = trip;

  }

}
