package com.isec.pokercli.controller.api.commands;

import com.isec.pokercli.controller.api.Command;

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
