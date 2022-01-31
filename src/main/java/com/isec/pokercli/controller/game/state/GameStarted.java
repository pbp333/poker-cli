package com.isec.pokercli.controller.game.state;

public class GameStarted implements GameState {

    @Override
    public GameState start() {
        return this;
    }

    @Override
    public GameState finish() {
        return new GameFinished();
    }
}
