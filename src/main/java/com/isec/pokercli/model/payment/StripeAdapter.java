package com.isec.pokercli.model.payment;

import java.math.BigDecimal;

public class StripeAdapter implements PaymentService {
    @Override
    public void addPayment(String user, BigDecimal amount) {
        System.out.println("Payment to Stripe: amount - " + amount + ", user - " + user);
    }

    @Override
    public void cancelPayment(String user, BigDecimal amount) {
        System.out.println("Cancelling payment to Stripe: amount - " + amount + ", user - " + user);
    }
}
