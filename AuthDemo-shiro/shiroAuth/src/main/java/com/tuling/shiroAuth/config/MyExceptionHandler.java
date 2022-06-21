package com.tuling.shiroAuth.config;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Object error(){
        return "系统开小差，请稍后再试";
    }
}
