package com.isec.pokercli.services.persistence.entity.game.card;

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

    public static DeckCard mapDeckCard(String card) {
        if (card == null) {
            return null;
        }

        String rankStr = card.substring(0, card.length() - 1);
        String suitStr = card.substring(card.length() - 1);

        Rank rank = Rank.getByCode(rankStr).orElseThrow(() -> new IllegalArgumentException(""));
        Suit suit = Suit.getByCode(suitStr).orElseThrow(() -> new IllegalArgumentException(""));

        return new DeckCard(rank, suit);
    }

    public String getDatabaseFormat() {
        return rank.getCode() + suit.getCode();
    }
}
