package com.isec.pokercli.view;

import com.isec.pokercli.controller.api.Command;
import com.isec.pokercli.controller.api.CommandHandler;
import com.isec.pokercli.controller.api.commands.RegisterUser;
import com.isec.pokercli.controller.command.CommandHandlerImpl;

public class InputResolver {

    private CommandHandler handler;

    public static void main(String[] args) {

        CommandHandler handler = new CommandHandlerImpl();

        // convert string to command

        Command command = new RegisterUser();

        handler.apply(command);
    }
}
