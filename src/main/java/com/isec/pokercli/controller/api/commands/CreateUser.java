package com.isec.pokercli.controller.api.commands;

import com.isec.pokercli.controller.api.Command;

public class CreateUser implements Command {

    private String name;

    public CreateUser(String name) {
        this.name = name;
    }

    @Override
    public void execute() {

    }

    @Override
    public void undo() {

    }
}
