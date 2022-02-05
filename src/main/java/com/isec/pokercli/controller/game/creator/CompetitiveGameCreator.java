package com.isec.pokercli.controller.game.creator;

import com.isec.pokercli.model.entity.game.Game;
import com.isec.pokercli.model.entity.game.GameType;

public class CompetitiveGameCreator extends GameCreatorDecorator {

    private final Integer maxPlayers;
    private final Integer buyIn;
    private final Integer initialPlayerPot;
    private final Integer bet;

    private static final GameType gameType = GameType.COMPETITIVE;

    public CompetitiveGameCreator(GameCreator creator, Integer maxPlayers, Integer buyIn,
                                  Integer initialPlayerPot, Integer bet) {
        super(creator);
        this.maxPlayers = maxPlayers;
        this.buyIn = buyIn;
        this.initialPlayerPot = initialPlayerPot;
        this.bet = bet;
    }

    @Override
    public Game.Builder builder() {
        return creator.builder()
                .maxPlayers(maxPlayers).buyIn(buyIn)
                .initialPlayerPot(initialPlayerPot).bet(bet)
                .gameType(gameType);
    }

}
