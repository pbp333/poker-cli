package com.isec.pokercli.controller.api.commands;

import com.isec.pokercli.controller.api.Command;
import com.isec.pokercli.controller.game.GameService;
import com.isec.pokercli.controller.game.GameServiceImpl;

public class CreateCompetitiveGame implements Command {

    private final String gameName;
    private final String owner;
    private final int maxNumberOfPlayers;
    private final Integer minimalBuyIn;
    private final Integer initialBet;

    private final GameService service;

    public CreateCompetitiveGame(String gameName, String owner, Integer maxNumberOfPlayers, Integer minimalBuyIn, Integer initialBet) {
        this.gameName = gameName;
        this.owner = owner;
        this.maxNumberOfPlayers = maxNumberOfPlayers;
        this.minimalBuyIn = minimalBuyIn;
        this.initialBet = initialBet;

        this.service = new GameServiceImpl();
    }

    @Override
    public void execute() {
        this.service.createCompetitiveGame(this.gameName, this.owner, this.maxNumberOfPlayers, this.minimalBuyIn,
                this.initialBet);
    }

    @Override
    public void undo() {
        this.service.deleteGameByName(this.gameName);
    }
}
