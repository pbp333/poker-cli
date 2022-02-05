package com.isec.pokercli.controller.game.creator;

import com.isec.pokercli.model.entity.game.Game;
import com.isec.pokercli.model.entity.game.GameStatus;
import com.isec.pokercli.model.entity.game.GameType;

public class FriendlyGameCreator implements GameCreator {

    private final Long ownerId;
    private final String gameName;
    private static final GameStatus gameStatus = GameStatus.CREATED;

    private static final int MAX_NUM_OF_PLAYERS = 3;
    private static final int MINIMUM_BUY_IN = 50;
    private static final int BET = 4;

    private static final GameType gameType = GameType.FRIENDLY;

    public FriendlyGameCreator(String gameName, Long ownerId) {
        this.gameName = gameName;
        this.ownerId = ownerId;
    }

    @Override
    public Game.Builder builder() {
        return Game.builder().name(this.gameName).ownerId(this.ownerId).status(gameStatus).maxPlayers(MAX_NUM_OF_PLAYERS)
                .initialPlayerPot(MINIMUM_BUY_IN).bet(BET).gameType(gameType);
    }
}
