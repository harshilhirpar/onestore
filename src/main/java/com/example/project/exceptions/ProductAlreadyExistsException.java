package com.example.project.exceptions;

public class ProductAlreadyExistsException extends RuntimeException{
    public ProductAlreadyExistsException(String message){
        super(message);
    }
}
