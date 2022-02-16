package com.isec.pokercli.application.game.creator;

import com.isec.pokercli.services.persistence.entity.game.Game;
import com.isec.pokercli.services.persistence.entity.game.GameStatus;
import com.isec.pokercli.services.persistence.entity.game.GameType;

public class FriendlyGameCreator implements GameCreator {

    private final Long ownerId;
    private final String gameName;
    private static final GameStatus gameStatus = GameStatus.CREATED;

    private static final int MAX_NUM_OF_PLAYERS = 3;
    private static final int BUY_IN = 0;
    private static final int INITIAL_PLAYER_POT = 50;
    private static final int BET = 4;

    private static final GameType gameType = GameType.FRIENDLY;

    public FriendlyGameCreator(String gameName, Long ownerId) {
        this.gameName = gameName;
        this.ownerId = ownerId;
    }

    @Override
    public Game.Builder builder() {
        return Game.builder().name(this.gameName).ownerId(this.ownerId).status(gameStatus).maxPlayers(MAX_NUM_OF_PLAYERS)
                .buyIn(BUY_IN).initialPlayerPot(INITIAL_PLAYER_POT).bet(BET).gameType(gameType);
    }
}
