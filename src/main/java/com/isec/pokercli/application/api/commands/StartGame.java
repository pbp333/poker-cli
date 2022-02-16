package com.isec.pokercli.application.api.commands;

import com.isec.pokercli.application.api.Command;
import com.isec.pokercli.application.game.GameService;
import com.isec.pokercli.application.game.GameServiceImpl;

public class StartGame implements Command {

    private final String gameName;

    private final GameService service;

    public StartGame(String gameName) {
        this.gameName = gameName;
        this.service = new GameServiceImpl();
    }

    @Override
    public void execute() {
        service.startGame(gameName);
    }

    @Override
    public void undo() {

    }
}
