package com.isec.pokercli.application.api.commands;

import com.isec.pokercli.application.api.Command;

public class Fold implements Command {

    private String username;

    public Fold(String username) {
        this.username = username;
    }

    @Override
    public void execute() {

    }

    @Override
    public void undo() {

    }
}
