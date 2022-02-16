package com.isec.pokercli.application.api.commands;

import com.isec.pokercli.application.api.Command;
import com.isec.pokercli.application.game.GameService;
import com.isec.pokercli.application.game.GameServiceImpl;

public class CreateFriendlyGame implements Command {

    private final String owner;
    private final String gameName;

    private final GameService service;

    public CreateFriendlyGame(String gameName, String owner) {
        this.gameName = gameName;
        this.owner = owner;
        this.service = new GameServiceImpl();
    }

    @Override
    public void execute() {
        this.service.createFriendlyGame(this.gameName, this.owner);
    }

    @Override
    public void undo() {
        this.service.deleteGameByName(this.gameName);
    }
}
