package com.isec.pokercli.controller.api.commands;

import com.isec.pokercli.controller.api.Command;
import com.isec.pokercli.controller.game.GameService;
import com.isec.pokercli.controller.game.GameServiceImpl;

public class JoinGame implements Command {

    private String username;
    private String game;

    private final GameService service;

    public JoinGame(String username, String game) {
        this.username = username;
        this.game = game;

        this.service = new GameServiceImpl();
    }

    @Override
    public void execute() {
        service.addPlayerToGame(username, game);
    }

    @Override
    public void undo() {
        service.removePlayerFromGame(username, game);

    }
}
