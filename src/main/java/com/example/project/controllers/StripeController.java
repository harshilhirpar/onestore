package com.example.project.controllers;

import com.google.gson.Gson;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequestMapping("/payment")
public class StripeController {

    static class CreatePaymentResponse {
        private String clientSecret;
        private String dpmCheckerLink;
        public CreatePaymentResponse(String clientSecret, String transactionId) {
            this.clientSecret = clientSecret;
            // [DEV]: For demo purposes only, you should avoid exposing the PaymentIntent ID in the client-side code.
            this.dpmCheckerLink = "https://dashboard.stripe.com/settings/payment_methods/review?transaction_id="+transactionId;
        }
    }

    private static Gson gson = new Gson();

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping()
    public String doPayment() throws StripeException {
        Stripe.apiKey = "sk_test_51QPnvAApUKh3krP5zozdsYEEq2tH1gD44ZpXUSXP0S0a5kvjKTI54pJW7pQSnqa18IAVQ1xm3utQpvbzXRwc0cIH00eDcA0jF7";
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(300L)
                .setCurrency("cad")
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods
                                .builder()
                                .setEnabled(true)
                                .build()
                )
                .build();

        PaymentIntent intent = PaymentIntent.create(params);
        CreatePaymentResponse paymentResponse = new CreatePaymentResponse(intent.getClientSecret(), intent.getId());
        return gson.toJson(paymentResponse);

    }
}


