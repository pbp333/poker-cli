package com.isec.pokercli.controller.game.state;

public class GameFinished implements GameState {
    @Override
    public GameState start() {
        // TODO log cant finish already finished
        return this;
    }

    @Override
    public GameState finish() {
        // TODO log cant finish already finished
        return this;
    }
}
