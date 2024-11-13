package com.example.project.controllers;

import com.example.project.entities.BusinessProfileEntity;
import com.example.project.entities.UserEntity;
import com.example.project.services.BusinessProfileServices;
import com.example.project.utils.GetAuthenticatedUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/business/profile")
public class BusinessProfileControllers {

    private final BusinessProfileServices businessProfileServices;

    public BusinessProfileControllers(BusinessProfileServices businessProfileServices) {
        this.businessProfileServices = businessProfileServices;
    }

    @PostMapping()
    @PreAuthorize("hasAnyRole('BUSINESS')")
    public ResponseEntity<BusinessProfileEntity> createBusinessProfile(@RequestBody BusinessProfileEntity businessProfileEntity){
        UserEntity currentUser = GetAuthenticatedUser.getAuthenticatedUser();
        return new ResponseEntity<>(businessProfileServices.createBusinessProfileService(currentUser, businessProfileEntity), HttpStatus.CREATED);
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('BUSINESS')")
    public ResponseEntity<Optional<BusinessProfileEntity>> getUserBusinessProfile(){
        UserEntity currentUser = GetAuthenticatedUser.getAuthenticatedUser();
        return new ResponseEntity<>(businessProfileServices.findUserBusinessProfileService(currentUser), HttpStatus.OK);
    }
}
