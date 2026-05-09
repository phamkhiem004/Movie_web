package com.example.movieproject.chillmovie.controller;


import com.example.movieproject.chillmovie.DTO.CreateUserRequest;
import com.example.movieproject.chillmovie.DTO.RegisterDTO;
import com.example.movieproject.chillmovie.DTO.UserDTO;
import com.example.movieproject.chillmovie.entity.Movie;
import com.example.movieproject.chillmovie.entity.User;
import com.example.movieproject.chillmovie.entity.UserStatus;
import com.example.movieproject.chillmovie.service.UserService;
import com.example.movieproject.config.Translator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @Operation(summary = "getAllUser", description = "description", responses = {
            @ApiResponse(responseCode = "201", description = "Get all user successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = @ExampleObject(name = "ex name", summary = "ex summary"
                            )))
    })
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.findAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RegisterDTO> createUser(@Valid @RequestBody CreateUserRequest user) {
        RegisterDTO u = this.userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(u);

    }

    @PatchMapping("/user/{id}/active")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<UserDTO> activeUser(@PathVariable @Min(value = 1) Long id) {
        return ResponseEntity.ok(userService.updateUserStatus(id, UserStatus.ACTIVE));
    }

    @PatchMapping("/user/{id}/block")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<UserDTO> blockUser(@PathVariable @Min(value = 1) Long id) {
        return ResponseEntity.ok(userService.updateUserStatus(id, UserStatus.BLOCKED));
    }


    @GetMapping("/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDTO> getUserById(@PathVariable @Min(value = 1) Long id) {
        UserDTO user = userService.getUserByID(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }


}
