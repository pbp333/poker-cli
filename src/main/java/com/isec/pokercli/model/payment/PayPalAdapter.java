package com.isec.pokercli.model.payment;

import java.math.BigDecimal;

public class PayPalAdapter implements PaymentService {

    @Override
    public void addPayment(String user, BigDecimal amount) {
        System.out.println("Payment to PayPal: amount - " + amount + ", user - " + user);
    }
}
