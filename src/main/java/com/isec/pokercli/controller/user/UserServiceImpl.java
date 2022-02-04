package com.isec.pokercli.controller.user;

import com.isec.pokercli.model.entity.user.User;
import com.isec.pokercli.model.payment.PaymentServiceFactory;

import java.math.BigDecimal;

public class UserServiceImpl implements UserService {

    @Override
    public void createUser(String name) {
        User u = new User();
        u.setName(name);
        u.setBalance(new BigDecimal(0));
        u.setVirtualBalance(new BigDecimal(0));
        u.create();
    }

    @Override
    public void removeUser(String name) {
        User u = User.getByName(name);
        if (u != null) {
            u.delete();
        } else {
            System.out.println("Couldn't find the user");
        }
    }

    @Override
    public void addPayment(String user, BigDecimal amount, String paymentMethod) {
        // TODO verify user exists

        PaymentServiceFactory.buildPaymentService(paymentMethod).addPayment(user, amount);
    }
}
