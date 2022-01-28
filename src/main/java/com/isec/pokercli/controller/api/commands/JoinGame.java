package com.isec.pokercli.controller.api.commands;

import com.isec.pokercli.controller.api.Command;

public class JoinGame implements Command {

    private String username;
    private String game;

    public JoinGame(String username, String game) {
        this.username = username;
        this.game = game;
    }

    @Override
    public void execute() {

    }

    @Override
    public void undo() {

    }
}
