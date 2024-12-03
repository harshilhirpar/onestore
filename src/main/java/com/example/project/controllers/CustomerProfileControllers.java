package com.example.project.controllers;

import com.example.project.dtos.CreateCustomerProfileDto;
import com.example.project.dtos.responses.CustomerProfileResponseDto;
import com.example.project.dtos.responses.ProductInCartResponseDto;
import com.example.project.dtos.responses.ReviewCartResponseDto;
import com.example.project.entities.CartEntity;
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
    public ResponseEntity<?> getProductByIdForCustomer(@PathVariable String productId){
        try{
           ProductEntity product = customerProfileServices.getProductFromId(productId);
           return new ResponseEntity<>(product, HttpStatus.FOUND);
        }catch (NotFoundException ex){
            return globalExceptionHandler.handleNotFoundException(ex);
        }
    }

    @GetMapping("/product/add/{productId}")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<?> addToCartProduct(@PathVariable String productId){
        UserEntity currentUser = GetAuthenticatedUser.getAuthenticatedUser();
        try{
            CartEntity cart = customerProfileServices.addToCart(currentUser.getId(), productId);
            return new ResponseEntity<>(cart, HttpStatus.CREATED);
        }catch(NotFoundException ex){
            return globalExceptionHandler.handleNotFoundException(ex);
        }catch (AlreadyExistsExceptions ex){
            return globalExceptionHandler.handleAlreadyExistException(ex);
        }
    }

    @GetMapping("/product/increase/quantity/{productId}")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<?> increaseQuantityInCart(@PathVariable String productId){
        UserEntity currentUser = GetAuthenticatedUser.getAuthenticatedUser();
        try{
            CartEntity cart = customerProfileServices.increaseQuantityInCart(currentUser.getId(), productId);
            return new ResponseEntity<>(cart, HttpStatus.CREATED);

        }catch (NotFoundException ex){
            return globalExceptionHandler.handleNotFoundException(ex);
        }
    }

    @GetMapping("/product/decrease/quantity/{productId}")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<?> decreaseQuantityInCart(@PathVariable String productId){
        UserEntity currentUser = GetAuthenticatedUser.getAuthenticatedUser();
        try{
            CartEntity cart = customerProfileServices.decreaseQuantityInCart(currentUser.getId(), productId);
            return new ResponseEntity<>(cart, HttpStatus.CREATED);

        }catch (NotFoundException ex){
            return globalExceptionHandler.handleNotFoundException(ex);
        }
    }

    @GetMapping("/product/cart")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<?> getAllProductsInCart(){
        UserEntity currentUser = GetAuthenticatedUser.getAuthenticatedUser();
        try{
            List<ProductInCartResponseDto> cartEntities = customerProfileServices.getAllProductsFromCart(currentUser.getId());
            return new ResponseEntity<>(cartEntities, HttpStatus.FOUND);
        }catch (NotFoundException ex){
            return globalExceptionHandler.handleNotFoundException(ex);
        }
    }

    @DeleteMapping("/product/cart/remove/{productId}")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<?> removeProductFromCart(@PathVariable String productId){
        UserEntity currentUser = GetAuthenticatedUser.getAuthenticatedUser();
        try{
            String isDeleted = customerProfileServices.removeProductFromCart(currentUser.getId(), productId);
            return new ResponseEntity<>(isDeleted, HttpStatus.OK);
        }catch (NotFoundException ex){
            return globalExceptionHandler.handleNotFoundException(ex);
        }
    }

    @GetMapping("/product/cart/review")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<?> reviewCart(){
        UserEntity currentUser = GetAuthenticatedUser.getAuthenticatedUser();
        try{
            ReviewCartResponseDto reviewCart = customerProfileServices.reviewCart(currentUser.getId());
            return new ResponseEntity<>(reviewCart, HttpStatus.OK);
        }catch (NotFoundException ex){
            return globalExceptionHandler.handleNotFoundException(ex);
        }
    }
}
