package com.isec.pokercli.controller.game;

import com.isec.pokercli.controller.game.creator.CompetitiveGameCreator;
import com.isec.pokercli.controller.game.creator.FriendlyGameCreator;
import com.isec.pokercli.controller.game.creator.GameCreator;
import com.isec.pokercli.model.entity.game.Game;
import com.isec.pokercli.model.entity.user.User;
import com.isec.pokercli.model.session.DbSessionManager;

public class GameServiceImpl implements GameService {

    @Override
    public void createCompetitiveGame(String gameName, String owner, int maxNumberOfPlayers, Integer minimalBuyIn, Integer initialBet) {

        var game = Game.getByName(gameName);

        if (game != null) {
            throw new IllegalArgumentException("Game name already exists");
        }

        var user = User.getByUsername(owner);

        if (user == null) {
            throw new IllegalArgumentException("User does not exist");
        }

        GameCreator creator = new FriendlyGameCreator(gameName, user.getId());

        creator = new CompetitiveGameCreator(creator, maxNumberOfPlayers, minimalBuyIn, initialBet);

        creator.builder().build();

        DbSessionManager.getUnitOfWork().commit();

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
    }

    @Override
    public void deleteGameByName(String username) {

        DbSessionManager.getUnitOfWork().commit();
    }
}
