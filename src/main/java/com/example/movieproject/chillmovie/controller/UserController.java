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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequestMapping("/user")
@RestController
@Tag(name = "User Controller")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "getAllUser", description = "Api get all user", responses = {
            @ApiResponse(responseCode = "201", description = "Get all user successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = @ExampleObject(name = "ex name", summary = "ex summary"
                            )))
    })
    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.findAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @Operation(summary = "Add user", description = "Api create new user")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RegisterDTO> createUser(@Valid @RequestBody CreateUserRequest user) {
        RegisterDTO u = this.userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(u);

    }

    @Operation(summary = "Active user", description = "Api active user")
    @PatchMapping("/{id}/active")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<UserDTO> activeUser(@PathVariable @Min(value = 1) Long id) {
        return ResponseEntity.ok(userService.updateUserStatus(id, UserStatus.ACTIVE));
    }

    @Operation(summary = "Block user", description = "Api block user")
    @PatchMapping("/{id}/block")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<UserDTO> blockUser(@PathVariable @Min(value = 1) Long id) {
        return ResponseEntity.ok(userService.updateUserStatus(id, UserStatus.BLOCKED));
    }


    @Operation(summary = "Get user", description = "Api get user by Id")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDTO> getUserById(@PathVariable @Min(1) Long id) {
        UserDTO user = userService.getUserByID(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }


}
