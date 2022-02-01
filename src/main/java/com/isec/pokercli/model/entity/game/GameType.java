package com.isec.pokercli.model.entity.game;

import java.util.Arrays;
import java.util.Optional;

public enum GameType {
    COMPETITIVE, FRIENDLY;

    public static Optional<GameType> getByString(String gameType) {
        return Arrays.stream(GameType.values()).filter(e -> e.name().equals(gameType)).findFirst();
    }
}
