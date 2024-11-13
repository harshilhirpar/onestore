package com.example.project.utils;

import com.example.project.entities.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class GetAuthenticatedUser {
    public static UserEntity getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserEntity)authentication.getPrincipal();
    }
}
