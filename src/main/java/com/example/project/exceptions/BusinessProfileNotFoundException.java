package com.example.project.exceptions;

public class BusinessProfileNotFoundException extends RuntimeException{
    public BusinessProfileNotFoundException(String message){
        super(message);
    }
}
