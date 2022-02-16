package com.isec.pokercli.application.api.commands;

import com.isec.pokercli.application.api.Command;
import com.isec.pokercli.application.user.UserService;
import com.isec.pokercli.application.user.UserServiceImpl;

public class Login implements Command {

    private final String username;

    private final UserService service;

    public Login(String username) {
        this.username = username;

        this.service = new UserServiceImpl();
    }

    @Override
    public void execute() {
        service.login(username);
    }

    @Override
    public void undo() {
        service.logout(username);
    }
}
