package com.example.project.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String username){
        super("User " + username + " Not Found");
    }
}
