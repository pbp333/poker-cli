package com.isec.pokercli.model.entity.game;

import java.util.Arrays;
import java.util.Optional;

public enum GameStatus {
    CREATED, ONGOING, FINISHED;

    public static Optional<GameStatus> getByString(String gameStatus) {
        return Arrays.stream(GameStatus.values()).filter(e -> e.name().equals(gameStatus)).findFirst();
    }
}
