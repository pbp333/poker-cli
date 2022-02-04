package com.isec.pokercli.controller.api.commands;

import com.isec.pokercli.controller.api.Command;
import com.isec.pokercli.controller.game.GameService;
import com.isec.pokercli.controller.game.GameServiceImpl;

import java.math.BigDecimal;

public class CreateGame implements Command {

    private final String type;
    private final int maxNumberOfPlayers;
    private final BigDecimal minimalBuyIn;

    private long id;

    private final GameService service;

    public CreateGame(String type, int maxNumberOfPlayers, BigDecimal minimalBuyIn) {
        this.type = type;
        this.maxNumberOfPlayers = maxNumberOfPlayers;
        this.minimalBuyIn = minimalBuyIn;

        this.service = new GameServiceImpl();
    }

    @Override
    public void execute() {
        this.id = this.service.createGame(this.type, this.maxNumberOfPlayers, this.minimalBuyIn);
    }

    @Override
    public void undo() {
        this.service.deleteGame(this.id);
    }
}
