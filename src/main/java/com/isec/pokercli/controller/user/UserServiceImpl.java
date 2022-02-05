package com.isec.pokercli.controller.user;

import com.isec.pokercli.model.entity.message.Message;
import com.isec.pokercli.model.entity.user.User;
import com.isec.pokercli.model.payment.PaymentServiceFactory;
import com.isec.pokercli.model.session.DbSessionManager;

import java.math.BigDecimal;

public class UserServiceImpl implements UserService {

    @Override
    public void createUser(String username) {
        User.from(username).login();
        DbSessionManager.getUnitOfWork().commit();
    }

    @Override
    public void removeUser(String username) {

        var user = User.getByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException("user does not exist");
        }

        user.remove();
        DbSessionManager.getUnitOfWork().commit();
    }

    @Override
    public void addPayment(String username, BigDecimal amount, String paymentMethod) {

        var user = User.getByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException("user does not exist");
        }

        BigDecimal amountAfterCut = amount.multiply(new BigDecimal(0.95));

        PaymentServiceFactory.buildPaymentService(paymentMethod).addPayment(username, amountAfterCut);
        user.addBalance(amountAfterCut);
        DbSessionManager.getUnitOfWork().commit();
    }

    @Override
    public void login(String username) {
        var user = User.getByUsername(username);
        user.login();

        user.read(Message.getNotReadByDestination(user.getId()));
        DbSessionManager.getUnitOfWork().commit();

    }

    @Override
    public void logout(String username) {
        User.getByUsername(username).logout();
        DbSessionManager.getUnitOfWork().commit();
    }
}
