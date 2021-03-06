package com.isec.pokercli.application.game.deck.hand;

import com.isec.pokercli.services.persistence.entity.game.card.DeckCard;
import com.isec.pokercli.application.game.deck.hand.resolver.HandCalculator;
import com.isec.pokercli.application.game.deck.hand.resolver.HandResult;

import java.util.ArrayList;
import java.util.List;

public class Hand {

    private final List<DeckCard> cards = new ArrayList<>();

    public void addCard(DeckCard card) {
        this.cards.add(card);
    }

    public HandResult bestResult() {
        return HandCalculator.score(this.cards);
    }

}
