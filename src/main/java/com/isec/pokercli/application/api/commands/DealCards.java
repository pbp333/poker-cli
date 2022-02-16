package com.isec.pokercli.application.api.commands;

import com.isec.pokercli.application.api.Command;
import com.isec.pokercli.application.gameplay.GameplayService;
import com.isec.pokercli.application.gameplay.GameplayServiceImpl;

public class DealCards implements Command {

    private String gameName;
    private GameplayService service;

    public DealCards(String gameName) {
        this.gameName = gameName;
        this.service = new GameplayServiceImpl();
    }

    @Override
    public void execute() {
        this.service.dealCards(this.gameName);
    }

    @Override
    public void undo() {
        // TODO: Implement
    }
}
