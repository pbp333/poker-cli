package com.isec.pokercli.controller.game;

public interface GameService {

    void createCompetitiveGame(String gameName, String owner, int maxNumberOfPlayers, Integer minimalBuyIn, Integer initialBet);

    void createFriendlyGame(String gameName, String owner);

    void deleteGameByName(String username);

    void listEligibleGamesByUser(String username);
}
