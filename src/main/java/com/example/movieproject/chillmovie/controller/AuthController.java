package com.example.movieproject.chillmovie.controller;


import com.example.movieproject.chillmovie.DTO.CreateUserRequest;
import com.example.movieproject.chillmovie.DTO.LoginDTO;
import com.example.movieproject.chillmovie.DTO.RegisterDTO;
import com.example.movieproject.chillmovie.DTO.ResLoginDTO;
import com.example.movieproject.chillmovie.service.UserService;
import com.example.movieproject.chillmovie.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
@Tag(name = "Auth Controller")
@RestController
public class AuthController {


    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil, UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }

    @Operation(summary = "Login", description = "API login")
    @PostMapping("/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO) {

        //Nạp input gồm username và password
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());

        // Xác thực người dùng => cần viết
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        String access_token = this.securityUtil.createToken(authentication);
        ResLoginDTO resLoginDTO = new ResLoginDTO();
        resLoginDTO.setToken(access_token);
        return ResponseEntity.ok().body(resLoginDTO);
    }

    @Operation(summary = "Register", description = "API register")
    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RegisterDTO> register(@Valid @RequestBody CreateUserRequest user) {
        RegisterDTO u = this.userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(u);

    }
}
