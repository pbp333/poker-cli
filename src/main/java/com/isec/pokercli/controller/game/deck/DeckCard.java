package com.isec.pokercli.controller.game.deck;

import com.isec.pokercli.controller.game.deck.card.Rank;
import com.isec.pokercli.controller.game.deck.card.Suit;

public class DeckCard {

    private final Rank rank;
    private final Suit suit;

    public DeckCard(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }
}
