package com.isec.pokercli.application.api.commands;

import com.isec.pokercli.application.api.Command;
import com.isec.pokercli.application.game.GameService;
import com.isec.pokercli.application.game.GameServiceImpl;

public class CreateCompetitiveGame implements Command {

    private final String gameName;
    private final String owner;
    private final int maxNumberOfPlayers;
    private final Integer buyIn;
    private final Integer initialPlayerPot;
    private final Integer bet;

    private final GameService service;

    public CreateCompetitiveGame(String gameName, String owner, Integer maxNumberOfPlayers,
                                 Integer buyIn, Integer initialPlayerPot, Integer bet) {
        this.gameName = gameName;
        this.owner = owner;
        this.maxNumberOfPlayers = maxNumberOfPlayers;
        this.buyIn = buyIn;
        this.initialPlayerPot = initialPlayerPot;
        this.bet = bet;

        this.service = new GameServiceImpl();
    }

    @Override
    public void execute() {
        this.service.createCompetitiveGame(this.gameName, this.owner, this.maxNumberOfPlayers, this.buyIn,
                this.initialPlayerPot, this.bet);
    }

    @Override
    public void undo() {
        this.service.deleteGameByName(this.gameName);
    }
}
