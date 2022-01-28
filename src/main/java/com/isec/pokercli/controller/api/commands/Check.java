package com.isec.pokercli.controller.api.commands;

import com.isec.pokercli.controller.api.Command;

public class Check implements Command {

    private String username;

    public Check(String username) {
        this.username = username;
    }

    @Override
    public void execute() {

    }

    @Override
    public void undo() {

    }
}
