package com.example.movieproject.chillmovie.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestResponse<T> {
    private int statusCode;
    private String error;

    private Object message;// message trả về có thẻ là string hoặc arraylist
    private T data;

}
