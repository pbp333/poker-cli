package com.isec.pokercli.controller.api.commands;

import com.isec.pokercli.controller.api.Command;

import java.math.BigDecimal;

public class CreateGame implements Command {

    private String type;
    private int maxNumberOfPlayers;
    private BigDecimal minimalBuyIn;

    public CreateGame(String type, int maxNumberOfPlayers, BigDecimal minimalBuyIn) {
        this.type = type;
        this.maxNumberOfPlayers = maxNumberOfPlayers;
        this.minimalBuyIn = minimalBuyIn;
    }

    @Override
    public void execute() {

    }

    @Override
    public void undo() {

    }
}
