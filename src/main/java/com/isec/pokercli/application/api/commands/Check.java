package com.isec.pokercli.application.api.commands;

import com.isec.pokercli.application.api.Command;

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
