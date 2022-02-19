package com.isec.pokercli.application.api.commands;

import com.isec.pokercli.application.api.Command;
import com.isec.pokercli.application.user.UserService;
import com.isec.pokercli.application.user.UserServiceImpl;

import java.math.BigDecimal;

public class AddBalance implements Command {

    private final String username;
    private final BigDecimal amount;
    private final String paymentMethod;

    private final UserService service;

    public AddBalance(String username, BigDecimal amount, String paymentMethod) {
        this.username = username;
        this.amount = amount;
        this.paymentMethod = paymentMethod;

        this.service = new UserServiceImpl();
    }

    @Override
    public void execute() {
        service.addPayment(username, amount, paymentMethod);

    }

    @Override
    public void undo() {
        throw new UnsupportedOperationException("Payment cannot be reverted... HAHAHAHAHAHA");
    }
}
