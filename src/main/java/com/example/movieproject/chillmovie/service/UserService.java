package com.example.movieproject.chillmovie.service;

import com.example.movieproject.chillmovie.entity.Movie;
import com.example.movieproject.chillmovie.entity.User;
import com.example.movieproject.chillmovie.respository.UserRepository;
import org.springframework.stereotype.Service;
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
    public boolean checkExistUser(@PathVariable User user) {
        return userRepository.existsById(user.getId());
    }
    public User getUserByID(Long id) {
        Optional<User> movieOptional = userRepository.findById(id);// do findById trả về Optional để tránh lỗi
        // NullPointerException khi không tìm thấy movie
        // với id đó
        // nếu movie tồn tại, trả về movie đó
        return movieOptional.orElse(null);
    }
    public User activeUser( Long id) {
        User existingUser = getUserByID(id);
        if(existingUser != null) {existingUser.setStatus("ACTIVE");
            return userRepository.save(existingUser);}
        return null;


    }




}
