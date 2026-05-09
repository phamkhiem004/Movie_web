package com.example.movieproject.chillmovie.controller;

import com.example.movieproject.chillmovie.DTO.ActorDTO;
import com.example.movieproject.chillmovie.DTO.CreateActorRequest;
import com.example.movieproject.chillmovie.DTO.UpdateActorRequest;
import com.example.movieproject.chillmovie.entity.Actor;
import com.example.movieproject.chillmovie.service.ActorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/actor")
@Tag(name = "Actor Controller")
@RestController
public class ActorController {

    private final ActorService actorService;

    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    @Operation(summary = "Get all actors", description = "API get all actors")
    @GetMapping("/")
    public ResponseEntity<List<ActorDTO>> getActors() {
        List<ActorDTO> actors = actorService.getAllActors();
        return ResponseEntity.ok(actors);

    }

    @Operation(summary = "Get actor detail", description = "API get actor detail")
    @GetMapping("/details/{id}")
    public ResponseEntity<ActorDTO> getActorById(@PathVariable Long id) {
        ActorDTO actor = actorService.getActorById(id);
        return ResponseEntity.ok(actor);
    }

    @Operation(summary = "Create new actor", description = "API create new actor")
    @PostMapping("/create")
    public ResponseEntity<ActorDTO> createActor(@RequestBody CreateActorRequest request) {
        ActorDTO createdActor = actorService.createActor(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdActor);
    }

    @Operation(summary = "Update actor", description = "API update actor")
    @PutMapping("/update/{id}")
    public ResponseEntity<ActorDTO> updateActor(@PathVariable Long id, @RequestBody UpdateActorRequest request) {
        ActorDTO updateActor = actorService.updateActor(id, request);
        return ResponseEntity.ok(updateActor);
    }

}
