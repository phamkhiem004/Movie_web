package com.example.movieproject.chillmovie.service;

import com.example.movieproject.chillmovie.entity.Movie;
import com.example.movieproject.chillmovie.entity.User;
import com.example.movieproject.chillmovie.respository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAllUsers() {return userRepository.findAll();}

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUserByID(Long id) {
        Optional<User> movieOptional = userRepository.findById(id);
        return movieOptional.orElse(null);
    }

    @Transactional
    public User activeUser( Long id) {
        User existingUser = getUserByID(id);
        if(existingUser != null) {existingUser.setStatus("ACTIVE");
            return userRepository.save(existingUser);}
        return null;



    }
    @Transactional
    public User blockUser(@PathVariable Long id) {
        User existingUser = getUserByID(id);
        if(existingUser != null) {existingUser.setStatus("BLOCKED");
        return userRepository.save(existingUser);}
        return null;
    }







}
