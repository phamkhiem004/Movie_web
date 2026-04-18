package com.example.movieproject.chillmovie.controller;

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
    public ResponseEntity<List<Actor>> getActors() {
        List<Actor> actors = actorService.getAllActors();
        return ResponseEntity.ok(actors);

    }
    @GetMapping("/actor/{id}")
    public ResponseEntity<Actor> getActorById(@PathVariable Long id) {
        Actor actor = actorService.getActorById(id);
        return ResponseEntity.ok(actor);
    }
    @PostMapping("/actor/create")
    public ResponseEntity<Actor> createActor(@RequestBody Actor actor) {
        Actor createdActor = actorService.createActor(actor);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdActor);
    }
    @PutMapping("/actor/{id}/update")
    public ResponseEntity<Actor> updateActor(@PathVariable Long id ,@RequestBody Actor actor) {
        Actor updateActor = actorService.updateActor(id, actor);
        return ResponseEntity.ok(updateActor);
    }

}
