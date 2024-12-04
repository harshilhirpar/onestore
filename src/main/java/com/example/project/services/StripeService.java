package com.example.project.services;

import com.example.project.dtos.responses.PaymentResponseDto;
import com.example.project.dtos.responses.ReviewCartResponseDto;
import com.example.project.entities.CartEntity;
import com.example.project.repositories.CartRepository;
import com.example.project.repositories.ProductRepository;
import com.google.gson.Gson;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StripeService {

    private final CustomerProfileServices customerProfileServices;
    private String clientSecret;
    private String dpmCheckerLink;
    private static final Gson gson = new Gson();

    public StripeService(
            CustomerProfileServices customerProfileServices
    ) {
        this.customerProfileServices = customerProfileServices;
    }

//    public static class CreatePaymentResponse{
//        private String clientSecret;
//        private String dpmCheckerLink;
//        private String userId;
//        private long totalAmount;
//
//        public CreatePaymentResponse(
//                String clientSecret,
//                String userId,
//                String transactionId,
//                long totalAmount
//        ){
//            this.clientSecret = clientSecret;
//            this.dpmCheckerLink = "https://dashboard.stripe.com/settings/payment_methods/review?transaction_id="+transactionId;
//            this.userId = userId;
//            this.totalAmount = totalAmount;
//        }
//    }

    public PaymentResponseDto doPayment(String userId) throws StripeException {
        System.out.println("sdfsdvc");
        ReviewCartResponseDto cartResponse = customerProfileServices.reviewCart(userId);
        System.out.println("djfnmcnvjfurjd");
        long totalWithTax = (long) cartResponse.getTotalWithTax();
        Stripe.apiKey = "sk_test_51QPnvAApUKh3krP5zozdsYEEq2tH1gD44ZpXUSXP0S0a5kvjKTI54pJW7pQSnqa18IAVQ1xm3utQpvbzXRwc0cIH00eDcA0jF7";
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((totalWithTax))
                .setCurrency("cad")
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods
                                .builder()
                                .setEnabled(true)
                                .build()
                )
                .build();
        System.out.println(params);
        PaymentIntent intent = PaymentIntent.create(params);
//        System.out.println(intent);
        PaymentResponseDto response = new PaymentResponseDto(
                intent.getClientSecret(),
                intent.getId(),
                userId,
                totalWithTax
        );
//        CreatePaymentResponse paymentResponse = new CreatePaymentResponse(intent.getClientSecret(), userId, intent.getId(), totalWithTax);
        System.out.println(response.getTotalAmount());
        return response;

    }
}
