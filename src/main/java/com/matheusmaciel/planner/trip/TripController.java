package com.matheusmaciel.planner.trip;

import com.matheusmaciel.planner.activities.Activity;
import com.matheusmaciel.planner.activities.ActivityData;
import com.matheusmaciel.planner.activities.ActivityRequestPayLoad;
import com.matheusmaciel.planner.activities.ActivityResponse;
import com.matheusmaciel.planner.activities.ActivityService;
import com.matheusmaciel.planner.link.LinkRequestPayLoad;
import com.matheusmaciel.planner.link.LinkResponse;
import com.matheusmaciel.planner.link.LinkService;
import com.matheusmaciel.planner.participant.Participant;
import com.matheusmaciel.planner.participant.ParticipantCreateResponse;
import com.matheusmaciel.planner.participant.ParticipantData;
import com.matheusmaciel.planner.participant.ParticipantRequestPayload;
import com.matheusmaciel.planner.participant.ParticipantService;


import java.util.UUID;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private LinkService linkService;

    @PostMapping("/create")
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequestPayload payload) {
        Trip newTrip = new Trip(payload);
        tripRepository.save(newTrip);
        this.participantService.registerParticipantsToEvent(payload.emails_to_invite(), newTrip);

        return ResponseEntity.ok().body(new TripCreateResponse(newTrip.getId()));

    }

    @PostMapping("/{id}/invite")
    public ResponseEntity<ParticipantCreateResponse> inviteParticipant(@PathVariable UUID id, @RequestBody ParticipantRequestPayload payload) {
        Optional<Trip> trip = this.tripRepository.findById(id);
        if(trip.isPresent()){
            Trip rawTrip = trip.get();
            
            ParticipantCreateResponse participantResponse = this.participantService.registerParticipantToEvent(payload.email(), rawTrip);

            if(rawTrip.getIsConfirmed()){
                this.participantService.triggerConfirmationEmailToParticipant(payload.email());
            }

            return ResponseEntity.ok(participantResponse);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/activities")
    public ResponseEntity<ActivityResponse> registerActivity(@PathVariable UUID id, @RequestBody ActivityRequestPayLoad payload) {
       Optional<Trip> trip = this.tripRepository.findById(id);

       if(trip.isPresent()){
           Trip rawTrip = trip.get();
           ActivityResponse activityResponse = this.activityService.registerActivity(payload, rawTrip);
           return ResponseEntity.ok(activityResponse);
    }

        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/{id}/links")
    public ResponseEntity<LinkResponse> registerLink(@PathVariable UUID id, @RequestBody LinkRequestPayLoad payload) {
        Optional<Trip> trip = this.tripRepository.findById(id);

        if(trip.isPresent()){
            Trip rawTrip = trip.get();
            LinkResponse linkResponse = this.activityService.registerLink(payload, rawTrip);
            return ResponseEntity.ok(linkResponse);
     }

         return ResponseEntity.notFound().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID id){
         Optional<Trip> trip = this.tripRepository.findById(id);
         return trip.map(ResponseEntity::ok)
         .orElseGet(() -> ResponseEntity.notFound().build());
        
     }

    @GetMapping("/{id}/confirm")
    public ResponseEntity<Trip> confirmTrip(@PathVariable UUID id){
        Optional<Trip> trip = this.tripRepository.findById(id);

        if(trip.isPresent()){
            Trip rawTrip = trip.get();
            rawTrip.setIsConfirmed(true);
            this.participantService.triggerConfirmationEmailToParticipants(id);

            this.tripRepository.save(rawTrip);


            return ResponseEntity.ok().body(rawTrip);
        }

        return ResponseEntity.notFound().build();

    }


    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantData>> getAllParticipants(@PathVariable UUID id){
        List<ParticipantData> participantsList = this.participantService.getAllParticipantsFromEvent(id);

        return ResponseEntity.ok().body(participantsList);
    }

    @GetMapping("/{id}/activities")
    public ResponseEntity<List<ActivityData>> getAllActivities(@PathVariable UUID id){
        List<ActivityData> activitiesList = this.activityService.getAllActivitiesFromId(id);

        return ResponseEntity.ok().body(activitiesList);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable UUID id, @RequestBody TripRequestPayload payload){
         Optional<Trip> trip = this.tripRepository.findById(id);

         if(trip.isPresent()){
            Trip rawTrip = trip.get();
            rawTrip.setDestination(payload.destination());
            rawTrip.setStartsAt(LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setEndsAt(LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME));
            this.tripRepository.save(rawTrip);
            return ResponseEntity.ok().body(rawTrip);
        }

        return ResponseEntity.notFound().build();
                                     
    }

    
}
