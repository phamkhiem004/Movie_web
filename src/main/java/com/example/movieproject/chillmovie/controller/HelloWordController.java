package com.example.movieproject.chillmovie.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.movieproject.chillmovie.util.error.IdInvalidException;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class HelloWordController {

    @GetMapping("/")
    public String helloWorld() throws IdInvalidException {
      throw new IdInvalidException("check");
    }

}
