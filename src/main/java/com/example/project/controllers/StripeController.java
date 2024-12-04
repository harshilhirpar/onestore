package com.example.project.controllers;

import com.example.project.dtos.responses.PaymentResponseDto;
import com.example.project.entities.UserEntity;
import com.example.project.services.StripeService;
import com.example.project.utils.GetAuthenticatedUser;
import com.google.gson.Gson;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/payment")
public class StripeController {

    private final StripeService stripeService;

    public StripeController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/checkout/{userId}")
    public PaymentResponseDto checkoutController(@PathVariable String userId) throws StripeException {
        System.out.println(userId);
        return stripeService.doPayment(userId);
    }
}


