package com.isec.pokercli.controller.api.commands;

import com.isec.pokercli.controller.api.Command;
import com.isec.pokercli.controller.game.GameService;
import com.isec.pokercli.controller.game.GameServiceImpl;

public class ListGames implements Command {

    private final String username;

    private final GameService service;

    public ListGames(String username) {
        this.username = username;
        this.service = new GameServiceImpl();
    }

    @Override
    public void execute() {
        service.listEligibleGamesByUser(username);
    }

    @Override
    public void undo() {

    }
}
