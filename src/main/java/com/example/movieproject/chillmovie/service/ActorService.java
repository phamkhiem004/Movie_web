package com.example.movieproject.chillmovie.service;

import com.example.movieproject.chillmovie.entity.Actor;
import com.example.movieproject.chillmovie.respository.ActorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class ActorService {
    private final ActorRepository actorRepository;

    public ActorService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }


    public Actor getActorById(@PathVariable Long id) {
        Optional<Actor> actorOptional = actorRepository.findById(id);

        return actorOptional.orElse(null);
    }

    public Actor createActor(Actor actor){
        return actorRepository.save(actor);
    }

    public List<Actor> getAllActors(){
        return actorRepository.findAll();
    }
    @Transactional
    public Actor updateActor(Long id, Actor actor){
        Actor existingActor = getActorById(id);
        if(existingActor != null){
            existingActor.setName(actor.getName());
            existingActor.setAvatarUrl(actor.getAvatarUrl());
            existingActor.setBiography(actor.getBiography());
            existingActor.setNationality(actor.getNationality());
            existingActor.setBirthDate(actor.getBirthDate());
            actorRepository.save(existingActor);
        }
        return null;
    }
}
