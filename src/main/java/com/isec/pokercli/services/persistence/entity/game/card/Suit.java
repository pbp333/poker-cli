package com.isec.pokercli.services.persistence.entity.game.card;

import java.util.Arrays;
import java.util.Optional;

public enum Suit {

    SPADES("S"),
    CLUBS("C"),
    HEARTS("H"),
    DIAMONDS("D");

    private String code;

    Suit(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static Optional<Suit> getByCode(String code) {
        return Arrays.stream(Suit.values()).filter(r -> r.getCode().equals(code)).findFirst();
    }

}
