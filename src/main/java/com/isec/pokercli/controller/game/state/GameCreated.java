package com.isec.pokercli.controller.game.state;

public class GameCreated implements GameState {
    @Override
    public GameState start() {
        return new GameStarted();
    }

    @Override
    public GameState finish() {
        // TODO check this behaviour
        return new GameFinished();
    }
}
