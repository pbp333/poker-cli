package com.isec.pokercli.controller.user;

import com.isec.pokercli.model.payment.PaymentServiceFactory;

import java.math.BigDecimal;

public class UserServiceImpl implements UserService {

    @Override
    public void createUser(String name) {

    }

    @Override
    public void removeUser(String name) {

    }

    @Override
    public void addPayment(String user, BigDecimal amount, String paymentMethod) {
        // TODO verify user exists

        PaymentServiceFactory.buildPaymentService(paymentMethod).addPayment(user, amount);
    }
}
