package com.isec.pokercli.controller.game;

import com.isec.pokercli.controller.game.state.GameCreated;
import com.isec.pokercli.controller.game.state.GameState;
import com.isec.pokercli.model.entity.game.Game;

public class GameStateMachine {

    private Game game;
    private GameState currentStatus;

    //TODO associate a new instance to each new game

    public GameStateMachine(Game game) {
        this.game = game;
        this.currentStatus = new GameCreated();
    }

    public void start() {
        this.currentStatus = this.currentStatus.start();
    }

    public void finish() {
        this.currentStatus = this.currentStatus.finish();
    }
}
