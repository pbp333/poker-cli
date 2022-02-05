package com.isec.pokercli.model.entity.game;

import java.util.Arrays;
import java.util.Optional;

public enum GameType {
    COMPETITIVE("competitive"),
    FRIENDLY("friendly");

    private final String type;

    GameType(String type) {
        this.type = type;
    }

    public static Optional<GameType> getByString(String type) {
        return Arrays.stream(GameType.values()).filter(e -> e.type.equals(type)).findFirst();
    }
}
