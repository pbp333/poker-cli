package com.isec.pokercli.application.user;

import com.isec.pokercli.services.payment.PaymentServiceFactory;
import com.isec.pokercli.services.persistence.entity.message.Message;
import com.isec.pokercli.services.persistence.entity.user.User;
import com.isec.pokercli.services.persistence.session.DbSessionManager;

import java.math.BigDecimal;

public class UserServiceImpl implements UserService {

    @Override
    public void createUser(String username) {
        User.from(username).login();
        DbSessionManager.getUnitOfWork().commit();
    }

    @Override
    public void removeUser(String username) {

        var user = User.getByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User is not valid"));

        user.remove();
        DbSessionManager.getUnitOfWork().commit();
    }

    @Override
    public void addPayment(String username, BigDecimal amount, String paymentMethod) {

        var user = User.getByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User is not valid"));

        BigDecimal amountAfterCut = amount.multiply(new BigDecimal(0.95));

        PaymentServiceFactory.buildPaymentService(paymentMethod).addPayment(username, amountAfterCut);
        user.addBalance(amountAfterCut);
        DbSessionManager.getUnitOfWork().commit();
    }

    @Override
    public void login(String username) {
        var user = User.getByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User is not valid"));
        user.login();

        user.read(Message.getNotReadByDestination(user.getId()));
        DbSessionManager.getUnitOfWork().commit();

    }

    @Override
    public void logout(String username) {
        User.getByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User is not valid"))
                .logout();
        DbSessionManager.getUnitOfWork().commit();
    }
}
