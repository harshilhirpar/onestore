package com.example.project.controllers;

import com.example.project.dtos.CreateCustomerProfileDto;
import com.example.project.dtos.responses.CustomerProfileResponseDto;
import com.example.project.entities.CustomerProfileEntity;
import com.example.project.entities.ProductEntity;
import com.example.project.entities.UserEntity;
import com.example.project.exceptions.AlreadyExistsExceptions;
import com.example.project.exceptions.GlobalExceptionHandler;
import com.example.project.exceptions.NotFoundException;
import com.example.project.services.CustomerProfileServices;
import com.example.project.utils.GetAuthenticatedUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerProfileControllers {

    private final CustomerProfileServices customerProfileServices;
    private final GlobalExceptionHandler globalExceptionHandler;

    public CustomerProfileControllers(
            CustomerProfileServices customerProfileServices,
            GlobalExceptionHandler globalExceptionHandler
    ) {
        this.customerProfileServices = customerProfileServices;
        this.globalExceptionHandler = globalExceptionHandler;
    }

    //    TODO: IMPLEMENT CREATE PROFILE
    @PostMapping("/profile")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<?> createCustomerProfile(@RequestBody CreateCustomerProfileDto createCustomerProfileDto){
        try{
            UserEntity currentUser = GetAuthenticatedUser.getAuthenticatedUser();
            CustomerProfileEntity customerProfile = customerProfileServices.createProfile(currentUser, createCustomerProfileDto);
            CustomerProfileResponseDto responseDto = new CustomerProfileResponseDto();
            responseDto.setError(false);
            responseDto.setMessage("Profile Created Successfully");
            responseDto.setCustomerProfile(customerProfile);
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        }catch (AlreadyExistsExceptions ex){
            return globalExceptionHandler.handleAlreadyExistException(ex);
        }

    }
//    TODO: IMPLEMENT UPDATE PROFILE
    @PutMapping("/update/profile")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<?> updateCustomerProfile(@RequestBody CreateCustomerProfileDto createCustomerProfileDto){
        try{
            UserEntity currentUser = GetAuthenticatedUser.getAuthenticatedUser();
            CustomerProfileEntity updatedCustomerProfile = customerProfileServices.updateProfile(currentUser, createCustomerProfileDto);
            CustomerProfileResponseDto responseDto = new CustomerProfileResponseDto();
            responseDto.setError(false);
            responseDto.setMessage("Profile Updated Successfully");
            responseDto.setCustomerProfile(updatedCustomerProfile);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch (NotFoundException ex){
            return globalExceptionHandler.handleNotFoundException(ex);
        }
    }
//    TODO: IMPLEMENT GET PROFILE WITH AUTH DETAILS
    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<?> getCustomerProfile(){
        try{
            UserEntity currentUser = GetAuthenticatedUser.getAuthenticatedUser();
            CustomerProfileEntity customerProfile = customerProfileServices.getProfile(currentUser);
            CustomerProfileResponseDto responseDto = new CustomerProfileResponseDto();
            responseDto.setError(false);
            responseDto.setMessage("Profile fetched Successfully");
            responseDto.setCustomerProfile(customerProfile);
            return new ResponseEntity<>(customerProfile, HttpStatus.OK);
        }catch (NotFoundException ex){
            return globalExceptionHandler.handleNotFoundException(ex);
        }
    }

//    TODO: GET ALL PRODUCTS, WITH PAGINATION
    @GetMapping("/products")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<?> getAllProductForCustomer(
            @RequestParam int page,
            @RequestParam int size
    ){
        List<ProductEntity> products = customerProfileServices.getAllProductForCustomer(page, size);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

//    TODO: SEE PARTICULAR PRODUCTS
    @GetMapping("/products/{productId}")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public void getProductByIdForCustomer(){}
}
