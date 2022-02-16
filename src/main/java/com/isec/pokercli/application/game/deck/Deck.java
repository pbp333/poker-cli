package com.isec.pokercli.application.game.deck;

import com.isec.pokercli.services.persistence.entity.game.card.DeckCard;

import java.util.Stack;

public class Deck {

    private final Stack<DeckCard> cards = new Stack<>();

    public Deck() {
        this.cards.addAll(DeckCardFactory.buildRandomDeck());
    }

    public DeckCard next() {
        return cards.pop();
    }

    public void discard() {
        cards.pop();
    }
}
