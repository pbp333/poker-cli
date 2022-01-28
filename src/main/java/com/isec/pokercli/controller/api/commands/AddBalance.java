package com.isec.pokercli.controller.api.commands;

import com.isec.pokercli.controller.api.Command;

import java.math.BigDecimal;

public class AddBalance implements Command {

    private String username;
    private BigDecimal amount;

    public AddBalance(String username, BigDecimal amount) {
        this.username = username;
        this.amount = amount;
    }

    @Override
    public void execute() {

    }

    @Override
    public void undo() {

    }
}
