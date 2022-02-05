package com.isec.pokercli.controller.game.deck.hand.scoring.scorers;

import com.isec.pokercli.model.entity.game.card.DeckCard;

import java.util.List;

public abstract class HandScorer {

    public int score(List<DeckCard> cards) {
        return resolve(cards);
    }

    protected abstract int resolve(List<DeckCard> cards);

}
