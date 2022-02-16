package com.isec.pokercli.services.payment;

public class PaymentServiceFactory {

    public static PaymentService buildPaymentService(String paymentMethod) {
        // TODO validate input

        if ("PayPal".equals(paymentMethod)) {
            return new PayPalAdapter();
        }

        if ("Stripe".equals(paymentMethod)) {
            return new StripeAdapter();
        }

        throw new IllegalArgumentException("Payment Method is not valid");
    }

}
