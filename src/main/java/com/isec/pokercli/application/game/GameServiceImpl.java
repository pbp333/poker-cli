package com.isec.pokercli.application.game;

import com.isec.pokercli.application.audit.AuditSearchImpl;
import com.isec.pokercli.application.audit.AuditService;
import com.isec.pokercli.application.game.creator.CompetitiveGameCreator;
import com.isec.pokercli.application.game.creator.FriendlyGameCreator;
import com.isec.pokercli.application.game.creator.GameCreator;
import com.isec.pokercli.services.persistence.entity.audit.Audit;
import com.isec.pokercli.services.persistence.entity.audit.AuditType;
import com.isec.pokercli.services.persistence.entity.game.Game;
import com.isec.pokercli.services.persistence.entity.game.GameRound;
import com.isec.pokercli.services.persistence.entity.user.User;
import com.isec.pokercli.services.persistence.session.DbSessionManager;

public class GameServiceImpl implements GameService {

    private final AuditService auditService;

    public GameServiceImpl() {
        this.auditService = new AuditSearchImpl();
    }

    @Override
    public void createCompetitiveGame(String gameName, String owner, int maxNumberOfPlayers, Integer buyIn,
                                      Integer initialPlayerPot, Integer initialBet) {

        var game = Game.getByName(gameName);

        if (game != null) {
            throw new IllegalArgumentException("Game name already exists");
        }

        var user = User.getByUsername(owner).orElseThrow(() -> new IllegalArgumentException("User is not valid"));

        GameCreator creator = new FriendlyGameCreator(gameName, user.getId());

        creator = new CompetitiveGameCreator(creator, maxNumberOfPlayers, buyIn, initialPlayerPot, initialBet);

        creator.builder().build();

        DbSessionManager.getUnitOfWork().commit();

        createGameRoundAssociatedWithGame(gameName);

        auditService.entry(Audit.builder().type(AuditType.GAME).owner(user).log("Competitive Game created").build());
    }

    @Override
    public void createFriendlyGame(String gameName, String owner) {
        var user = User.getByUsername(owner).orElseThrow(() -> new IllegalArgumentException("User is not valid"));

        var creator = new FriendlyGameCreator(gameName, user.getId());

        creator.builder().build();

        DbSessionManager.getUnitOfWork().commit();

        createGameRoundAssociatedWithGame(gameName);

        auditService.entry(Audit.builder().type(AuditType.GAME).owner(user).log("Friendly Game created").build());
    }

    private void createGameRoundAssociatedWithGame(String gameName) {
        // create an empty game round associated with the game
        Game game = Game.getByName(gameName);

        if (game == null) {
            throw new IllegalArgumentException("Game is invalid");
        }

        var user = User.getById(game.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("User is not valid, this should not happen"));

        GameRound gr = new GameRound();
        gr.setGameId(game.getId());
        gr.create();

        auditService.entry(Audit.builder().type(AuditType.GAME).owner(user).log("Game deleted").build());
    }

    @Override
    public void deleteGameByName(String gameName) {

        var game = Game.getByName(gameName);

        if (game == null) {
            throw new IllegalArgumentException("Game does not exist");
        }

        var user = User.getById(game.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("User is not valid, this should not happen"));

        game.markAsDeleted();

        DbSessionManager.getUnitOfWork().commit();

        auditService.entry(Audit.builder().type(AuditType.GAME).owner(user).log("Game deleted").build());
    }

    @Override
    public void listEligibleGamesByUser(String username) {

        var user = User.getByUsername(username).orElseThrow(() -> new IllegalArgumentException("User is not valid"));

        var games = Game.getByMinimumBalance(user.getBalance());

        games.forEach(System.out::println);

        auditService.entry(Audit.builder().type(AuditType.GAME).owner(user).log("Listing eligible games").build());
    }

    @Override
    public void addPlayerToGame(String username, String gameName) {

        //TODO check user is not in another game

        var user = User.getByUsername(username).orElseThrow(() -> new IllegalArgumentException("User is not valid"));

        var game = Game.getByName(gameName);

        if (game == null) {
            throw new IllegalArgumentException("Game does not exist");
        }

        if (game.getUsers().size() >= game.getMaxPlayers()) {
            throw new IllegalStateException("Game is full");
        }

        game.addUser(user);

        DbSessionManager.getUnitOfWork().commit();

        auditService.entry(Audit.builder().type(AuditType.GAME).owner(user).log("User added to Game").build());
    }

    @Override
    public void removePlayerFromGame(String username, String gameName) {
        var user = User.getByUsername(username).orElseThrow(() -> new IllegalArgumentException("User is not valid"));

        var game = Game.getByName(gameName);

        if (game == null) {
            throw new IllegalArgumentException("Game does not exist");
        }

        game.removePlayer(user);

        DbSessionManager.getUnitOfWork().commit();

        auditService.entry(Audit.builder().type(AuditType.GAME).owner(user).log("User removed from Game").build());
    }

    @Override
    public void startGame(String gameName) {

        var game = Game.getByName(gameName);

        if (game == null) {
            throw new IllegalArgumentException("Game does not exist");
        }

        var user = User.getById(game.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("User is not valid, this should not happen"));

        auditService.entry(Audit.builder().type(AuditType.GAME).owner(user).log("Game started").build());
    }
}
