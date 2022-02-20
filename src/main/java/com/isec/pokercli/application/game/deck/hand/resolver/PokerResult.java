package com.isec.pokercli.application.game.deck.hand.resolver;

import java.util.Arrays;

public enum PokerResult {

    HIGH_CARD(1),
    ONE_PAIR(100),
    TWO_PAIR(500),
    THREE_OF_A_KIND(1000),
    STRAIGHT(2000),
    FLUSH(4000),
    FULL_HOUSE(8000),
    FOUR_OF_A_KIND(16000),
    STRAIGHT_FLUSH(32000);

    private final int score;

    PokerResult(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

}
