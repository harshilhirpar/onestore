package com.example.project.exceptions;

import com.example.project.enums.ErrorMessagesEnum;

public class CustomerProfileNotFoundException extends RuntimeException{
    public CustomerProfileNotFoundException(String message){
        super(String.valueOf(ErrorMessagesEnum.CUSTOMER_PROFILE_NOT_FOUND));
    }
}
