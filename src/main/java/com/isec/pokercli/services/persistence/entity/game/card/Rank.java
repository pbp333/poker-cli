package com.isec.pokercli.services.persistence.entity.game.card;

import java.util.Arrays;
import java.util.Optional;

public enum Rank {

    ACE(13, "A"),
    KING(12, "K"),
    QUEEN(11, "Q"),
    JACK(10, "J"),
    TEN(9, "10"),
    NINE(8, "9"),
    EIGHT(7, "8"),
    SEVEN(6, "7"),
    SIX(5, "6"),
    FIVE(4, "5"),
    FOUR(3, "4"),
    THREE(2, "3"),
    TWO(1, "2");

    private int score;
    private String code;

    Rank(int score, String code) {
        this.score = score;
        this.code = code;
    }

    public int getScore() {
        return score;
    }

    public String getCode() {
        return code;
    }

    public static Optional<Rank> getByCode(String code) {
        return Arrays.stream(Rank.values()).filter(r -> r.getCode().equals(code)).findFirst();
    }
}
