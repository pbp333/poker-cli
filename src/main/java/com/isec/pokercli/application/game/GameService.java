package com.isec.pokercli.application.game;

import java.math.BigDecimal;

public interface GameService {

    void createCompetitiveGame(String gameName, String owner, int maxNumberOfPlayers, Integer buyIn, Integer initialPlayerPot, Integer bet);

    void createFriendlyGame(String gameName, String owner);

    void deleteGameByName(String username);

    void listEligibleGamesByUser(String username);

    void addPlayerToGame(String username, String game);

    void removePlayerFromGame(String username, String game);

    void startGame(String game);

    void bet(String username, BigDecimal amount);

    void check(String username);

    void fold(String username);

    void calculateWinner(String gameName);
}
