package com.isec.pokercli.controller.api.commands;

import com.isec.pokercli.controller.api.Command;
import com.isec.pokercli.controller.gameplay.GameplayService;
import com.isec.pokercli.controller.gameplay.GameplayServiceImpl;

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
