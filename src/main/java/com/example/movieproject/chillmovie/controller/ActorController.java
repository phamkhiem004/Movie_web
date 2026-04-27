package com.example.movieproject.chillmovie.controller;

import com.example.movieproject.chillmovie.DTO.ActorDTO;
import com.example.movieproject.chillmovie.DTO.CreateActorRequest;
import com.example.movieproject.chillmovie.DTO.UpdateActorRequest;
import com.example.movieproject.chillmovie.entity.Actor;
import com.example.movieproject.chillmovie.service.ActorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ActorController {

    private final ActorService actorService;

    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    @GetMapping("/actors")
    public ResponseEntity<List<ActorDTO>> getActors() {
        List<ActorDTO> actors = actorService.getAllActors();
        return ResponseEntity.ok(actors);

    }

    @GetMapping("/actor/{id}")
    public ResponseEntity<ActorDTO> getActorById(@PathVariable Long id) {
        ActorDTO actor = actorService.getActorById(id);
        return ResponseEntity.ok(actor);
    }

    @PostMapping("/actor/create")
    public ResponseEntity<ActorDTO> createActor(@RequestBody CreateActorRequest request) {
        ActorDTO createdActor = actorService.createActor(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdActor);
    }

    @PutMapping("/actor/{id}/update")
    public ResponseEntity<ActorDTO> updateActor(@PathVariable Long id, @RequestBody UpdateActorRequest request) {
        ActorDTO updateActor = actorService.updateActor(id, request);
        return ResponseEntity.ok(updateActor);
    }

}
