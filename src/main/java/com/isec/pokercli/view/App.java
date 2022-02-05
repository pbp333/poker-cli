package com.isec.pokercli.view;

import com.isec.pokercli.controller.api.commands.*;
import com.isec.pokercli.controller.command.CommandHandlerImpl;
import com.isec.pokercli.view.actions.UserAction;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        System.out.println("Starting poker-cli...");

        var sc = new Scanner(System.in);

        var handler = new CommandHandlerImpl();

        var running = true;
        while (running) {
            String input = sc.nextLine();

            String[] inputSplit = input.split(" ");
            if (inputSplit.length == 0) {
                System.out.println("Invalid input");
                continue;
            }

            Optional<UserAction> c = UserAction.getByCommandString(inputSplit[0]);
            if (c.isEmpty()) {
                continue;
            }

            try {
                switch (c.get()) {
                    case CREATE_FRIENDLY_GAME:
                        handler.apply(new CreateFriendlyGame(inputSplit[1], inputSplit[2]));
                        break;
                    case CREATE_COMPETITIVE_GAME:
                        handler.apply(new CreateCompetitiveGame(inputSplit[1], inputSplit[2],
                                Integer.parseInt(inputSplit[3]), Integer.parseInt(inputSplit[4]),
                                Integer.parseInt(inputSplit[5])));
                        break;
                    case LIST_GAMES:
                        handler.apply(new ListGames(inputSplit[1]));
                        break;
                    case JOIN_GAME:
                        handler.apply(new JoinGame(inputSplit[1], inputSplit[2]));
                        break;
                    case START_GAME:
                        handler.apply(new StartGame());
                        break;
                    case BET:
                        handler.apply(new Bet(inputSplit[1], new BigDecimal(inputSplit[2])));
                        break;
                    case FOLD:
                        handler.apply(new Fold(inputSplit[1]));
                        break;
                    case CHECK:
                        handler.apply(new Check(inputSplit[1]));
                        break;
                    case SHUFFLE_DECK:
                        handler.apply(new ShuffleDeck());
                        break;
                    case DEAL_CARDS:
                        handler.apply(new DealCards());
                        break;
                    case CREATE_USER:
                        handler.apply(new CreateUser(inputSplit[1]));
                        break;
                    case LOGIN:
                        handler.apply(new Login(inputSplit[1]));
                        break;
                    case LOGOUT:
                        handler.apply(new Logout(inputSplit[1]));
                        break;
                    case ADD_BALANCE:
                        handler.apply(new AddBalance(inputSplit[1], new BigDecimal(inputSplit[2]), inputSplit[3]));
                        break;
                    case MESSAGE:
                        handler.apply(new SendMessage(inputSplit[1], inputSplit[2], inputSplit[3]));
                        break;
                    case UNDO:
                        handler.undo();
                        break;
                    case REDO:
                        handler.redo();
                        break;
                    case EXIT:
                        running = false;
                        break;
                }
            } catch (Throwable e) {
                System.out.println("User action invalid - " + input);
                System.out.println(e.getMessage());
            }
        }
        System.out.println("Exit");
    }
}
