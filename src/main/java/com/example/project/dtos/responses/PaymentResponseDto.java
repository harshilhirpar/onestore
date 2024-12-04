package com.example.project.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PaymentResponseDto {
    private String clientSecret;
    private String dpmCheckerLink;
    private String userId;
    private long totalAmount;

    public PaymentResponseDto(
            String clientSecret,
            String transactionId,
            String userId,
            long totalAmount
    ){
        this.clientSecret = clientSecret;
        this.dpmCheckerLink = "https://dashboard.stripe.com/settings/payment_methods/review?transaction_id="+transactionId;
        this.userId = userId;
        this.totalAmount = totalAmount;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getDpmCheckerLink() {
        return dpmCheckerLink;
    }

    public void setDpmCheckerLink(String dpmCheckerLink) {
        this.dpmCheckerLink = dpmCheckerLink;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }
}
