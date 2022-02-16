package com.isec.pokercli.application.api.commands;

import com.isec.pokercli.application.api.Command;
import com.isec.pokercli.application.user.UserService;
import com.isec.pokercli.application.user.UserServiceImpl;

public class CreateUser implements Command {

    private final String name;
    private final UserService service;

    public CreateUser(String name) {
        this.name = name;
        this.service = new UserServiceImpl();
    }

    @Override
    public void execute() {
        this.service.createUser(this.name);
    }

    @Override
    public void undo() {
        this.service.removeUser(this.name);
    }
}
