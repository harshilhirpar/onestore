package com.example.project.controllers;
import com.example.project.entities.UserEntity;
import com.example.project.exceptions.GlobalExceptionHandler;
import com.example.project.exceptions.NotFoundException;
import com.example.project.services.BusinessProfileActivitiesServices;
import com.example.project.utils.GetAuthenticatedUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/business/activity")
public class BusinessPersonActivitiesControllers {

    private final BusinessProfileActivitiesServices businessProfileActivitiesServices;
    private final GlobalExceptionHandler globalExceptionHandler;

    public BusinessPersonActivitiesControllers(
            BusinessProfileActivitiesServices businessProfileActivitiesServices,
            GlobalExceptionHandler globalExceptionHandler
    ){
        this.businessProfileActivitiesServices = businessProfileActivitiesServices;
        this.globalExceptionHandler = globalExceptionHandler;
    }

    @GetMapping("/sold/products")
    @PreAuthorize("hasAnyRole('BUSINESS')")
    public ResponseEntity<?> getAllSoldProducts(){
        try{
            UserEntity currentUser = GetAuthenticatedUser.getAuthenticatedUser();
            return new ResponseEntity<>(businessProfileActivitiesServices.getAllSoldProducts(currentUser), HttpStatus.FOUND);
        }catch (NotFoundException ex){
            return globalExceptionHandler.handleNotFoundException(ex);
        }
    }
}
