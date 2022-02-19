package com.isec.pokercli.cli.actions;

import java.util.Arrays;
import java.util.Optional;

public enum UserAction {

    // game related
    CREATE_FRIENDLY_GAME("createFriendlyGame"),
    CREATE_COMPETITIVE_GAME("createCompetitiveGame"),
    LIST_GAMES("listGames"),
    JOIN_GAME("joinGame"),
    START_GAME("startGame"),
    BET("bet"),
    FOLD("fold"),
    CHECK("check"),
    SHUFFLE_DECK("shuffleDeck"),
    DEAL_CARDS("dealCards"),

    // user related
    CREATE_USER("createUser"),
    LOGIN("login"),
    LOGOUT("logout"),
    ADD_BALANCE("addBalance"),
    MESSAGE("message"),

    // audits
    AUDIT("audit"), // username, type, limit

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
