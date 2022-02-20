package com.isec.pokercli.application.api.commands;

import com.isec.pokercli.application.api.Command;
import com.isec.pokercli.application.game.GameService;
import com.isec.pokercli.application.game.GameServiceImpl;

public class Fold implements Command {

    private String username;

    private final GameService service;

    public Fold(String username) {
        this.username = username;

        this.service = new GameServiceImpl();
    }

    @Override
    public void execute() {
        service.fold(username);
    }

    @Override
    public void undo() {
        throw new IllegalArgumentException("Cannot undo a fold");
    }
}
