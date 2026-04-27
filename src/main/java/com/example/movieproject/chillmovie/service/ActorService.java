package com.example.movieproject.chillmovie.service;

import com.example.movieproject.chillmovie.DTO.ActorDTO;
import com.example.movieproject.chillmovie.DTO.CreateActorRequest;
import com.example.movieproject.chillmovie.DTO.UpdateActorRequest;
import com.example.movieproject.chillmovie.entity.Actor;
import com.example.movieproject.chillmovie.entity.Movie;
import com.example.movieproject.chillmovie.respository.ActorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ActorService {
    private final ActorRepository actorRepository;

    public ActorService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }


    public ActorDTO getActorById(@PathVariable Long id) {
        Actor actor = actorRepository.findById(id).orElse(null);
        ActorDTO actorDTO = new ActorDTO();
        assert actor != null;
        actorDTO.setId(actor.getId());
        actorDTO.setName(actor.getName());
        actorDTO.setNationality(actor.getNationality());
        actorDTO.setBirthDate(actor.getBirthDate());
        actorDTO.setBio(actor.getBiography());
        actorDTO.setAvatarUrl(actor.getAvatarUrl());


        return actorDTO;
    }

    public ActorDTO createActor(CreateActorRequest request) {
        Actor actor = new Actor();
        actor.setName(request.name);
        actor.setNationality(request.nationality);
        actor.setBirthDate(request.birthDate);
        actor.setBiography(request.bio);
        actor.setAvatarUrl(request.avatarUrl);
        actorRepository.save(actor);

        ActorDTO actorDTO = new ActorDTO();
        actorDTO.setName(actor.getName());
        actorDTO.setBirthDate(actor.getBirthDate());
        actorDTO.setNationality(actor.getNationality());
        actorDTO.setBio(actor.getBiography());
        actorDTO.setAvatarUrl(actor.getAvatarUrl());
        return actorDTO;
    }

    public List<ActorDTO> getAllActors(){
        List<Actor> actors = actorRepository.findAll();
        return actors.stream().map( map ->{
            ActorDTO dto = new ActorDTO();
            dto.setId(map.getId());
            dto.setName(map.getName());
            dto.setBirthDate(map.getBirthDate());
            dto.setBio(map.getBiography());
            dto.setAvatarUrl(map.getAvatarUrl());
            return dto;
        }).toList();
    }
    @Transactional
    public ActorDTO updateActor(Long id, UpdateActorRequest request){
        Actor existingActor = actorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Actor not found"));
        ActorDTO actorDTO = new ActorDTO();
        if(existingActor != null){
            existingActor.setName(request.name);
            existingActor.setAvatarUrl(request.avatarUrl);
            existingActor.setBiography(request.bio);
            existingActor.setNationality(request.nationality);
            existingActor.setBirthDate(request.birthDate);
            actorRepository.save(existingActor);


            actorDTO.setId(existingActor.getId());
            actorDTO.setName(existingActor.getName());
            actorDTO.setNationality(existingActor.getNationality());
            actorDTO.setBirthDate(existingActor.getBirthDate());
            actorDTO.setBio(existingActor.getBiography());
            actorDTO.setAvatarUrl(existingActor.getAvatarUrl());

        }
        return actorDTO;
    }
}
