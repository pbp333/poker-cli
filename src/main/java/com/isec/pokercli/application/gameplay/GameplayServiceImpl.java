package com.isec.pokercli.application.gameplay;

import com.isec.pokercli.application.game.deck.Deck;
import com.isec.pokercli.services.persistence.entity.game.Game;
import com.isec.pokercli.services.persistence.entity.game.GameRound;
import com.isec.pokercli.services.persistence.entity.game.GameStatus;
import com.isec.pokercli.services.persistence.entity.game.GameUser;

public class GameplayServiceImpl implements GameplayService {

    @Override
    public void dealCards(String gameName) {
        Game game = Game.getByName(gameName)
                .orElseThrow(() -> new IllegalArgumentException("Game is not valid"));

        if (!game.getStatus().equals(GameStatus.ONGOING)) {
            throw new IllegalStateException("Game is not ongoing");
        }

        Deck deck = new Deck();

        GameRound gr = GameRound.getByGameId(game.getId());
        gr.setCard1(deck.next());
        gr.setCard2(deck.next());
        gr.setCard3(deck.next());
        gr.setCard4(deck.next());
        gr.setCard5(deck.next());
        gr.update();

        game.getUsers().forEach(u -> {
            GameUser gu = GameUser.getByUserId(u.getId());
            gu.setCard1(deck.next());
            gu.setCard2(deck.next());
            gu.update();
        });
    }
}
