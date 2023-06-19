package com.isec.pokercli.application.game.deck.hand.scoring.scorers;

import com.isec.pokercli.services.persistence.entity.game.card.DeckCard;

import java.util.List;

public abstract class HandScorer {

    public int score(List<DeckCard> cards) {
        return resolve(cards);
    }


    protected abstract int resolve(List<DeckCard> cards);

}
