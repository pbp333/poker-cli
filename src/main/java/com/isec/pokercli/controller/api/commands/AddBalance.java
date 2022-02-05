package com.isec.pokercli.controller.api.commands;

import com.isec.pokercli.controller.api.Command;
import com.isec.pokercli.controller.user.UserService;
import com.isec.pokercli.controller.user.UserServiceImpl;

import java.math.BigDecimal;

public class AddBalance implements Command {

    private String username;
    private BigDecimal amount;
    private String paymentMethod;

    private UserService service;

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
