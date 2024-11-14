package com.example.project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<String> handleRoleNotFoundException(RoleNotFoundException ex){
        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex){
        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericExceptions(Exception ex){
        return new ResponseEntity<>(
                "An Unexpected Error Occurred " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(BusinessProfileNotFoundException.class)
    public ResponseEntity<String> handleBusinessProfileNotFoundException(BusinessProfileNotFoundException ex){
        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyException(UserAlreadyExistsException ex){
        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<String> handleProductAlreadyExistsException(ProductAlreadyExistsException ex){
        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

}
