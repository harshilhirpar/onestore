package com.example.project.exceptions;

public class RoleNotFoundException extends RuntimeException{
    public RoleNotFoundException(String roleName){
        super("Role " + roleName + " Not Found");
    }
}

