package com.isec.pokercli.application.game;

import com.isec.pokercli.application.game.creator.CompetitiveGameCreator;
import com.isec.pokercli.application.game.creator.FriendlyGameCreator;
import com.isec.pokercli.application.game.creator.GameCreator;
import com.isec.pokercli.services.persistence.entity.game.Game;
import com.isec.pokercli.services.persistence.entity.game.GameRound;
import com.isec.pokercli.services.persistence.entity.user.User;
import com.isec.pokercli.services.persistence.session.DbSessionManager;

public class GameServiceImpl implements GameService {

    @Override
    public void createCompetitiveGame(String gameName, String owner, int maxNumberOfPlayers, Integer buyIn,
                                      Integer initialPlayerPot, Integer initialBet) {

        var game = Game.getByName(gameName);

        if (game != null) {
            throw new IllegalArgumentException("Game name already exists");
        }

        var user = User.getByUsername(owner);

        if (user == null) {
            throw new IllegalArgumentException("User does not exist");
        }

        GameCreator creator = new FriendlyGameCreator(gameName, user.getId());

        creator = new CompetitiveGameCreator(creator, maxNumberOfPlayers, buyIn, initialPlayerPot, initialBet);

        creator.builder().build();

        DbSessionManager.getUnitOfWork().commit();

        createGameRoundAssociatedWithGame(gameName);
    }

    @Override
    public void createFriendlyGame(String gameName, String owner) {
        var user = User.getByUsername(owner);

        if (user == null) {
            throw new IllegalArgumentException("User does not exist");
        }

        var creator = new FriendlyGameCreator(gameName, user.getId());

        creator.builder().build();

        DbSessionManager.getUnitOfWork().commit();

        createGameRoundAssociatedWithGame(gameName);
    }

    private void createGameRoundAssociatedWithGame(String gameName) {
        // create an empty game round associated with the game
        Game g = Game.getByName(gameName);

        if (g == null) {
            throw new IllegalArgumentException("Game is invalid");
        }

        GameRound gr = new GameRound();
        gr.setGameId(g.getId());
        gr.create();
    }

    @Override
    public void deleteGameByName(String username) {

        DbSessionManager.getUnitOfWork().commit();
    }

    @Override
    public void listEligibleGamesByUser(String username) {

        var user = User.getByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException("User does not exist");
        }


        var games = Game.getByMinimumBalance(user.getBalance());

        games.forEach(System.out::println);
    }

    @Override
    public void addPlayerToGame(String username, String gameName) {

        //TODO check user is not in another game

        var user = User.getByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException("User does not exist");
        }

        var game = Game.getByName(gameName);

        if (game == null) {
            throw new IllegalArgumentException("Game does not exist");
        }

        if (game.getUsers().size() >= game.getMaxPlayers()) {
            throw new IllegalStateException("Game is full");
        }

        game.addUser(user);

        DbSessionManager.getUnitOfWork().commit();
    }

    @Override
    public void removePlayerFromGame(String username, String gameName) {
        var user = User.getByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException("User does not exist");
        }

        var game = Game.getByName(gameName);

        if (game == null) {
            throw new IllegalArgumentException("Game does not exist");
        }

        game.removePlayer(user);

        DbSessionManager.getUnitOfWork().commit();
    }

    @Override
    public void startGame(String gameName) {

    }
}
