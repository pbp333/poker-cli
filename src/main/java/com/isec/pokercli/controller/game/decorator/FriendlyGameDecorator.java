package com.isec.pokercli.controller.game.decorator;

import com.isec.pokercli.model.entity.game.GameType;

public class FriendlyGameDecorator extends GameDecorator {

    public FriendlyGameDecorator(GameDecorator game) {
        super(game);
    }

    @Override
    public GameType getGameType() {
        return GameType.FRIENDLY;
    }
}
