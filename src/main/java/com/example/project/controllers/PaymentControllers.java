package com.example.project.controllers;

import com.example.project.dtos.InitiatePaymentRequestDto;
import com.example.project.entities.TransactionsEntity;
import com.example.project.entities.UserEntity;
import com.example.project.exceptions.GlobalExceptionHandler;
import com.example.project.exceptions.NotFoundException;
import com.example.project.exceptions.PaymentExceptions;
import com.example.project.exceptions.SomethingWentWrongException;
import com.example.project.services.PaymentServices;
import com.example.project.utils.GetAuthenticatedUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentControllers {
    private final PaymentServices paymentServices;
    private final GlobalExceptionHandler globalExceptionHandler;

    public PaymentControllers(
            PaymentServices paymentServices,
            GlobalExceptionHandler globalExceptionHandler
    ){
        this.paymentServices = paymentServices;
        this.globalExceptionHandler = globalExceptionHandler;
    }

//    TODO: Initiate Payment Set the Status to Pending
    @PostMapping("/initiate")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<?> initiatePaymentController(@RequestBody InitiatePaymentRequestDto requestDto){
        try{
            UserEntity currentUser = GetAuthenticatedUser.getAuthenticatedUser();
            TransactionsEntity initiatedTransaction = paymentServices.initiatePaymentService(currentUser, requestDto.getPaymentType());
            return new ResponseEntity<>(initiatedTransaction, HttpStatus.OK);
        }catch (PaymentExceptions ex){
            return globalExceptionHandler.handlePaymentExceptions(ex);
        }
    }
//    TODO: Endpoint for Payment Success, if Success then do the transactions in the DB
    @GetMapping("/success")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<?> paymentSuccessController(){
        try{
            UserEntity currentUser = GetAuthenticatedUser.getAuthenticatedUser();
            TransactionsEntity successfulTransaction = paymentServices.paymentSuccessService(currentUser);
            return new ResponseEntity<>(successfulTransaction, HttpStatus.OK);
        }catch (SomethingWentWrongException ex){
            return globalExceptionHandler.handleSomethingWentWrongException(ex);
        }catch (NotFoundException ex){
            return globalExceptionHandler.handleNotFoundException(ex);
        }
    }
//    TODO: Endpoint for Payment Failure
    @PostMapping("/failure")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public void paymentFailureController(){}
}
