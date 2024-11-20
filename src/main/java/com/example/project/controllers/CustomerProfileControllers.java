package com.example.project.controllers;

import com.example.project.dtos.CreateCustomerProfileDto;
import com.example.project.entities.UserEntity;
import com.example.project.utils.GetAuthenticatedUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerProfileControllers {
//    TODO: IMPLEMENT CREATE PROFILE
    @PostMapping("/profile")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<?> createCustomerProfile(@RequestBody CreateCustomerProfileDto createCustomerProfileDto){
        UserEntity currentUser = GetAuthenticatedUser.getAuthenticatedUser();
        return new ResponseEntity<>("DONE", HttpStatus.CREATED);
    }
//    TODO: IMPLEMENT UPDATE PROFILE
    @PutMapping("/update/profile")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<?> updateCustomerProfile(@RequestBody CreateCustomerProfileDto createCustomerProfileDto){
        UserEntity currentUser = GetAuthenticatedUser.getAuthenticatedUser();
        return new ResponseEntity<>("DONE", HttpStatus.OK);
    }
//    TODO: IMPLEMENT GET PROFILE WITH AUTH DETAILS
    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<?> getCustomerProfile(){
        UserEntity currentUser = GetAuthenticatedUser.getAuthenticatedUser();
        return new ResponseEntity<>("DONE", HttpStatus.OK);
    }
}
