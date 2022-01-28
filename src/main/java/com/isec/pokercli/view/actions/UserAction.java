package com.isec.pokercli.view.actions;

import java.util.Arrays;
import java.util.Optional;

public enum UserAction {

    // game related
    CREATE_GAME("createGame"),
    JOIN_GAME("joinGame"),
    START_GAME("startGame"),
    BET("bet"),
    FOLD("fold"),
    CHECK("check"),
    SHUFFLE_DECK("shuffleDeck"),
    DEAL_CARDS("setCards"),

    // user related
    CREATE_USER("createUser"),
    ADD_BALANCE("addBalance"),
    MESSAGE("message"),

    // others
    UNDO("undo"),
    REDO("redo"),
    EXIT("exit");

    private String input;

    UserAction(String input) {
        this.input = input;
    }

    public static Optional<UserAction> getByCommandString(String input) {
        return Arrays.stream(UserAction.values()).filter(c -> c.input.equals(input)).findFirst();
    }
}
