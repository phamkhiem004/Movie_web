package com.example.movieproject.chillmovie.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.movieproject.chillmovie.service.error.IdInvalidException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class HelloWordController {

    @GetMapping("/")
    public String helloWorld() throws IdInvalidException {
      throw new IdInvalidException("check");
    }

}
