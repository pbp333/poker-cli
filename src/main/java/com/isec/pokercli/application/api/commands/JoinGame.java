package com.isec.pokercli.application.api.commands;

import com.isec.pokercli.application.api.Command;
import com.isec.pokercli.application.game.GameService;
import com.isec.pokercli.application.game.GameServiceImpl;

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
