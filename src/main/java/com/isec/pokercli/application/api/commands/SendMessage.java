package com.isec.pokercli.application.api.commands;

import com.isec.pokercli.application.api.Command;
import com.isec.pokercli.application.message.MessageService;
import com.isec.pokercli.application.message.MessageServiceImpl;

public class SendMessage implements Command {

    private String from;
    private String to;
    private String message;

    private MessageService service;

    public SendMessage(String from, String to, String message) {
        this.from = from;
        this.to = to;
        this.message = message;

        this.service = new MessageServiceImpl();
    }

    @Override
    public void execute() {
        this.service.deliverMessage(this.from, this.to, this.message);
    }

    @Override
    public void undo() {
        this.service.deleteMessage(this.from, this.to, this.message);
    }
}
