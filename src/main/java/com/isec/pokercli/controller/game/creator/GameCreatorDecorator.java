package com.isec.pokercli.controller.game.creator;

import com.isec.pokercli.model.entity.game.Game;

public abstract class GameCreatorDecorator implements GameCreator {

    protected GameCreator creator;

    public GameCreatorDecorator(GameCreator creator) {
        this.creator = creator;
    }

    @Override
    public Game.Builder builder() {
        return creator.builder();
    }

}
