package com.example.movieproject.chillmovie.controller;


import com.example.movieproject.chillmovie.entity.Movie;
import com.example.movieproject.chillmovie.entity.User;
import com.example.movieproject.chillmovie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @PostMapping("/user/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User u = this.userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(u);

    }
    @PatchMapping("user/{id}/active")
    public ResponseEntity<User> updateUser(@PathVariable Long id) {
        User updateUser = userService.activeUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(updateUser);
    }


}
