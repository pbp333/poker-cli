package com.isec.pokercli.cli.controller;

import com.isec.pokercli.application.api.Command;
import com.isec.pokercli.application.api.CommandHandler;
import com.isec.pokercli.application.api.commands.*;
import com.isec.pokercli.application.command.CommandHandlerImpl;
import com.isec.pokercli.cli.actions.ExitAction;
import com.isec.pokercli.cli.actions.UserAction;

import java.math.BigDecimal;
import java.util.Arrays;

public class CliControllerImpl implements CliController {

    private final CommandHandler handler;

    public CliControllerImpl() {
        this.handler = new CommandHandlerImpl();
    }


    @Override
    public void handleAction(String[] action) throws ExitAction {

        if (action.length == 0) {
            throw new IllegalArgumentException("Action is not valid");
        }

        var c = UserAction.getByCommandString(action[0]);
        if (c.isEmpty()) {
            throw new IllegalArgumentException("Action is not valid");
        }

        switch (c.get()) {
            case CREATE_FRIENDLY_GAME:
                handler.apply(new CreateFriendlyGame(action[1], action[2]));
                break;
            case CREATE_COMPETITIVE_GAME:
                handler.apply(new CreateCompetitiveGame(action[1], action[2],
                        Integer.parseInt(action[3]), Integer.parseInt(action[4]),
                        Integer.parseInt(action[5]), Integer.parseInt(action[6])));
                break;
            case LIST_GAMES:
                handler.apply(new ListGames(action[1]));
                break;
            case JOIN_GAME:
                handler.apply(new JoinGame(action[1], action[2]));
                break;
            case START_GAME:
                handler.apply(new StartGame(action[1]));
                break;
            case BET:
                handler.apply(new Bet(action[1], new BigDecimal(action[2])));
                break;
            case FOLD:
                handler.apply(new Fold(action[1]));
                break;
            case CHECK:
                handler.apply(new Check(action[1]));
                break;
            case SHUFFLE_DECK:
                handler.apply(new ShuffleDeck());
                break;
            case DEAL_CARDS:
                handler.apply(new DealCards(action[1]));
                break;
            case CREATE_USER:
                handler.apply(new CreateUser(action[1]));
                break;
            case LOGIN:
                handler.apply(new Login(action[1]));
                break;
            case LOGOUT:
                handler.apply(new Logout(action[1]));
                break;
            case ADD_BALANCE:
                handler.apply(new AddBalance(action[1], new BigDecimal(action[2]), action[3]));
                break;
            case MESSAGE:
                handler.apply(new SendMessage(action[1], action[2], action[3]));
                break;
            case AUDIT:
                handler.apply(buildAuditSearchCommand(action));
                break;
            case UNDO:
                handler.undo();
                break;
            case REDO:
                handler.redo();
                break;
            case EXIT:
                throw new ExitAction();
        }

    }

    private Command buildAuditSearchCommand(String[] action) {
        var username = Arrays.stream(action).filter(act -> act.contains("user:")).findFirst()
                .map(act -> act.split("user:")).map(array -> array[1]).orElse("");

        var type = Arrays.stream(action).filter(act -> act.contains("type:")).findFirst()
                .map(act -> act.split("type:")).map(array -> array[1]).orElse("");

        var numberOfMessages = Arrays.stream(action).filter(act -> act.contains("limit:")).findFirst()
                .map(act -> act.split("limit:")).map(array -> array[1]).map(lmt -> Integer.valueOf(lmt))
                .orElse(null);

        return new AuditEntrySearch(numberOfMessages, username, type);

    }
}
