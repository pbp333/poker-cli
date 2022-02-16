package com.isec.pokercli.services.persistence.entity.game;

import java.util.Arrays;
import java.util.Optional;

public enum GameType {
    COMPETITIVE,
    FRIENDLY;

    public static Optional<GameType> getByString(String type) {
        return Arrays.stream(GameType.values()).filter(e -> e.name().equals(type)).findFirst();
    }
}
