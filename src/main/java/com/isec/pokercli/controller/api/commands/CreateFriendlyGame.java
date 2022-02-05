package com.isec.pokercli.controller.api.commands;

import com.isec.pokercli.controller.api.Command;
import com.isec.pokercli.controller.game.GameService;
import com.isec.pokercli.controller.game.GameServiceImpl;

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
