package com.isec.pokercli.services.persistence.entity.game;

import java.util.Arrays;

public enum GameUserStatus {

    DEFAULT(0), CHECK(1), FOLD(2);

    private int status;

    GameUserStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public static GameUserStatus getByInt(int gameStatusInt) {
        return Arrays.stream(GameUserStatus.values())
                .filter(e -> e.getStatus() == gameStatusInt)
                .findFirst()
                .orElse(GameUserStatus.DEFAULT);
    }
}
