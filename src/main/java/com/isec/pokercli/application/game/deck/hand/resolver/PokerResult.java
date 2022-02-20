package com.isec.pokercli.application.game.deck.hand.resolver;

import java.util.Arrays;

public enum PokerResult {

    HIGH_CARD(1),
    ONE_PAIR(200),
    TWO_PAIR(300),
    THREE_OF_A_KIND(400),
    STRAIGHT(500),
    FLUSH(600),
    FULL_HOUSE(700),
    FOUR_OF_A_KIND(800),
    STRAIGHT_FLUSH(900);

    private final int score;

    PokerResult(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public static String getResultStringByScore(int score) {
        PokerResult result = Arrays.stream(PokerResult.values())
                .filter(e -> e.getScore() == score)
                .findFirst()
                .orElse(HIGH_CARD);

        return result.name();
    }
}
