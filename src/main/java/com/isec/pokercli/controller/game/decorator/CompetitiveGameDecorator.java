package com.isec.pokercli.controller.game.decorator;

import com.isec.pokercli.model.entity.game.GameType;

public class CompetitiveGameDecorator extends GameDecorator {

    public CompetitiveGameDecorator(GameDecorator game) {
        super(game);
    }

    @Override
    public GameType getGameType() {
        return GameType.COMPETITIVE;
    }
}
