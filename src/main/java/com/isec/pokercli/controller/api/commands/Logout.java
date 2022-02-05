package com.isec.pokercli.controller.api.commands;

import com.isec.pokercli.controller.api.Command;
import com.isec.pokercli.controller.user.UserService;
import com.isec.pokercli.controller.user.UserServiceImpl;

public class Logout implements Command {

    private final String username;

    private final UserService service;

    public Logout(String username) {
        this.username = username;

        this.service = new UserServiceImpl();
    }

    @Override
    public void execute() {
        service.logout(username);
    }

    @Override
    public void undo() {
        service.login(username);
    }
}
