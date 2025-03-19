package com.matheusmaciel.planner.trip;

import com.matheusmaciel.planner.participant.ParticipantService;

import java.lang.foreign.Linker.Option;
import java.util.UUID;

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

    @PostMapping("/create")
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequestPayload payload) {
        Trip newTrip = new Trip(payload);
        tripRepository.save(newTrip);
        this.participantService
        .registerParticipantsToEvent(payload.emails_to_invite(),
                                     newTrip.getId());

        return ResponseEntity.ok().body(new TripCreateResponse(newTrip.getId()));

    }

    @GetMapping("/{id}")
     public ResponseEntity<Trip> getTripDetails(@PathVariable UUID id){
         Optional<Trip> trip = this.tripRepository.findById(id);
         return trip.map(ResponseEntity::ok)
         .orElseGet(() -> ResponseEntity.notFound().build());
        
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
