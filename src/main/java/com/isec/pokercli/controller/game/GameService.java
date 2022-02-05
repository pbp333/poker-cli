package com.isec.pokercli.controller.game;

public interface GameService {

    void createCompetitiveGame(String gameName, String owner, int maxNumberOfPlayers, Integer buyIn, Integer initialPlayerPot, Integer bet);

    void createFriendlyGame(String gameName, String owner);

    void deleteGameByName(String username);

    void listEligibleGamesByUser(String username);

    void addPlayerToGame(String username, String game);

    void removePlayerFromGame(String username, String game);

    void startGame(String game);
}
