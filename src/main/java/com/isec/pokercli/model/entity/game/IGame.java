package com.isec.pokercli.model.entity.game;

import java.time.LocalDateTime;

public interface IGame {

    Long getId();

    Long getUserId();

    GameType getGameType();

    Integer getMaxPlayers();

    Integer getChips();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();

    GameStatus getStatus();

}
