package com.isec.pokercli.application.api.commands;

import com.isec.pokercli.application.api.Command;
import com.isec.pokercli.application.game.GameService;
import com.isec.pokercli.application.game.GameServiceImpl;

import java.math.BigDecimal;

public class Bet implements Command {

    private String username;
    private BigDecimal amount;

    private final GameService service;

    public Bet(String username, BigDecimal amount) {
        this.username = username;
        this.amount = amount;

        this.service = new GameServiceImpl();
    }

    @Override
    public void execute() {
        service.bet(username, amount);
    }

    @Override
    public void undo() {
        throw new IllegalStateException("Cannot undo a bet");
    }
}
