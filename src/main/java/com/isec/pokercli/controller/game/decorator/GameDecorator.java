package com.isec.pokercli.controller.game.decorator;

import com.isec.pokercli.model.entity.game.GameType;

public abstract class GameDecorator {

    private GameDecorator game;

    public GameDecorator(GameDecorator game) {
        this.game = game;
    }

    public abstract GameType getGameType();
}
