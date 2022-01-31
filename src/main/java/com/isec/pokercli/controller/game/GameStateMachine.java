package com.isec.pokercli.controller.game;

import com.isec.pokercli.controller.game.state.GameCreated;
import com.isec.pokercli.controller.game.state.GameState;

public class GameStateMachine {

    private String game;
    private GameState currentStatus;

    //TODO associate a new instance to each new game

    public GameStateMachine(String game) {
        this.game = game;
        this.currentStatus = new GameCreated();
    }

    public void start() {
        this.currentStatus.start();
    }

    public void finish() {
        this.currentStatus.finish();
    }
}
