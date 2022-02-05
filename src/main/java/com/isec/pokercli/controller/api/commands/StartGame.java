package com.isec.pokercli.controller.api.commands;

import com.isec.pokercli.controller.api.Command;
import com.isec.pokercli.controller.game.GameService;
import com.isec.pokercli.controller.game.GameServiceImpl;

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
