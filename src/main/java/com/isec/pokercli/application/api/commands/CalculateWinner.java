package com.isec.pokercli.application.api.commands;

import com.isec.pokercli.application.api.Command;
import com.isec.pokercli.application.game.GameService;
import com.isec.pokercli.application.game.GameServiceImpl;

public class CalculateWinner implements Command {

    private String game;

    private final GameService service;

    public CalculateWinner(String game) {
        this.game = game;

        this.service = new GameServiceImpl();
    }

    @Override
    public void execute() {
        service.calculateWinner(game);
    }

    @Override
    public void undo() {
        throw new IllegalArgumentException("Cannot undo calculate winner");
    }
}
