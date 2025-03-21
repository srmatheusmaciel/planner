package com.matheusmaciel.planner.participant;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/participants")
public class ParticipantController {

  @Autowired
  private ParticipantRepository participantRepository;


  @GetMapping("/{id}/confirm")
  public ResponseEntity<Participant> confirmParticipant(@PathVariable UUID id, @RequestBody ParticipantRequestPayload payload) {
    Optional<Participant> participant = this.participantRepository.findById(id);

    if(participant.isPresent()){
        Participant rawParticipant = participant.get();
        rawParticipant.setName(payload.name());
        rawParticipant.setIsConfirmed(true);
        this.participantRepository.save(rawParticipant);
        return ResponseEntity.ok().body(rawParticipant);
    }
    
    
    return ResponseEntity.notFound().build();
  }


}
