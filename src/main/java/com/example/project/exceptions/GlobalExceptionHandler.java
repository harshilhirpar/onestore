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

//    @ExceptionHandler(Unknown.class)
    public ResponseEntity<String> handleUserNotFoundException(UserExceptions ex){
        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.UNAUTHORIZED
        );
    }

//    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericExceptions(Exception ex){
        return new ResponseEntity<>(
                "An Unexpected Error Occurred " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

//    @ExceptionHandler(BusinessProfileExceptions.class)
    public ResponseEntity<String> handleBusinessProfileNotFoundException(BusinessProfileExceptions ex){
        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

//    @ExceptionHandler(UserExceptions.class)
    public ResponseEntity<String> handleUserAlreadyException(UserExceptions ex){
        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

//    @ExceptionHandler(ProductExceptions.class)
    public ResponseEntity<String> handleProductAlreadyExistsException(ProductExceptions ex){
        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

//    @ExceptionHandler(BusinessProfileExceptions.class)
    public ResponseEntity<String> handleBusinessProfileAlreadyExists(BusinessProfileExceptions ex){
        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

//    @ExceptionHandler(ProductExceptions.class)
    public ResponseEntity<?> handleProductNotFound(ProductExceptions ex) {
        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }
}
