package com.isec.pokercli.controller.api.commands;

import com.isec.pokercli.controller.api.Command;

public class Message implements Command {

    private String from;
    private String to;
    private String message;

    public Message(String from, String to, String message) {
        this.from = from;
        this.to = to;
        this.message = message;
    }

    @Override
    public void execute() {

    }

    @Override
    public void undo() {

    }
}
