package com.isec.pokercli.application.game;

import com.isec.pokercli.application.game.state.GameCreated;
import com.isec.pokercli.application.game.state.GameState;
import com.isec.pokercli.services.persistence.entity.game.Game;

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
