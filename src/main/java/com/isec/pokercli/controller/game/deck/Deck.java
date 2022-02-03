package com.isec.pokercli.controller.game.deck;

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
