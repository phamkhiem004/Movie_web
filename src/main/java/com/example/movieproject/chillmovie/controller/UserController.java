package com.example.movieproject.chillmovie.controller;


import com.example.movieproject.chillmovie.DTO.CreateUserRequest;
import com.example.movieproject.chillmovie.DTO.UserDTO;
import com.example.movieproject.chillmovie.entity.Movie;
import com.example.movieproject.chillmovie.entity.User;
import com.example.movieproject.chillmovie.entity.UserStatus;
import com.example.movieproject.chillmovie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.findAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @PostMapping("/user/create")
    public ResponseEntity<UserDTO> createUser(@RequestBody CreateUserRequest user) {
        UserDTO u = this.userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(u);

    }

    @PatchMapping("/user/{id}/active")
    public ResponseEntity<UserDTO> activeUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.updateUserStatus(id, UserStatus.ACTIVE));
    }

    @PatchMapping("/user/{id}/block")
    public ResponseEntity<UserDTO> blockUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.updateUserStatus(id, UserStatus.BLOCKED));
    }


    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserByID(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }


}
