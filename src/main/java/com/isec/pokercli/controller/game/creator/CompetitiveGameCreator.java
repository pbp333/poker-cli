package com.isec.pokercli.controller.game.creator;

import com.isec.pokercli.model.entity.game.Game;
import com.isec.pokercli.model.entity.game.GameType;

public class CompetitiveGameCreator extends GameCreatorDecorator {

    private final Integer maxPlayers;
    private final Integer minimumBuyIn;
    private final Integer bet;

    private static final GameType gameType = GameType.COMPETITIVE;

    public CompetitiveGameCreator(GameCreator creator, Integer maxPlayers, Integer minimumBuyIn, Integer bet) {
        super(creator);
        this.maxPlayers = maxPlayers;
        this.minimumBuyIn = minimumBuyIn;
        this.bet = bet;
    }

    @Override
    public Game.Builder builder() {
        return creator.builder().maxPlayers(maxPlayers).initialPlayerPot(minimumBuyIn).bet(bet).gameType(gameType);
    }

}
