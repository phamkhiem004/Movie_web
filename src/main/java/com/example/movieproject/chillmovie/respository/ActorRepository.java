package com.example.movieproject.chillmovie.respository;

import com.example.movieproject.chillmovie.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActorRepository extends JpaRepository<Actor,Long> {

    Optional<Actor> findByIdAndIsDeletedFalse(Long id);

    List<Actor> findByIsDeletedFalse();
}
