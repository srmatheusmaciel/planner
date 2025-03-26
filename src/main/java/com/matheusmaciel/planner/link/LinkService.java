package com.matheusmaciel.planner.link;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matheusmaciel.planner.activities.Activity;
import com.matheusmaciel.planner.activities.ActivityRequestPayLoad;
import com.matheusmaciel.planner.activities.ActivityResponse;
import com.matheusmaciel.planner.trip.Trip;



@Service
public class LinkService {
  
  @Autowired
  private LinkRepository repository;
  
  public LinkResponse registerLink(LinkRequestPayLoad payload, Trip trip) {
    Link newLink = new Link(payload.title(), payload.url(), trip);

    this.repository.save(newLink);
    return new LinkResponse(newLink.getId());
  }

 
}
