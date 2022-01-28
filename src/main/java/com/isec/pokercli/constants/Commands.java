package com.isec.pokercli.constants;

import java.util.Arrays;
import java.util.Optional;

public enum Commands {

    // game related
    CREATE_GAME("createGame"),
    JOIN_GAME("joinGame"),
    START_GAME("startGame"),
    BET("bet"),
    FOLD("fold"),
    CHECK("check"),
    SHUFFLE_DECK("shuffleDeck"),
    SET_CARDS("setCards"),

    // user related
    CREATE_USER("createUser"),
    ADD_BALANCE("addBalance"),
    MESSAGE("message"),

    // others
    UNDO("undo"),
    EXIT("exit");

    private String action;

    Commands(String action) {
        this.action = action;
    }

    public static Optional<Commands> getByCommandString(String commandString) {
        return Arrays.stream(Commands.values()).filter(c -> c.action.equals(commandString)).findFirst();
    }
}
