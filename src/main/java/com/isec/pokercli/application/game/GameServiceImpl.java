package com.isec.pokercli.application.game;

import com.isec.pokercli.application.audit.AuditSearchImpl;
import com.isec.pokercli.application.audit.AuditService;
import com.isec.pokercli.application.game.creator.CompetitiveGameCreator;
import com.isec.pokercli.application.game.creator.FriendlyGameCreator;
import com.isec.pokercli.application.game.creator.GameCreator;
import com.isec.pokercli.application.game.deck.hand.resolver.HandCalculator;
import com.isec.pokercli.application.game.deck.hand.resolver.HandResult;
import com.isec.pokercli.application.game.deck.hand.resolver.PokerResult;
import com.isec.pokercli.services.persistence.entity.audit.Audit;
import com.isec.pokercli.services.persistence.entity.audit.AuditType;
import com.isec.pokercli.services.persistence.entity.game.*;
import com.isec.pokercli.services.persistence.entity.game.card.DeckCard;
import com.isec.pokercli.services.persistence.entity.user.User;
import com.isec.pokercli.services.persistence.session.DbSessionManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class GameServiceImpl implements GameService {

    private final AuditService auditService;

    public GameServiceImpl() {
        this.auditService = new AuditSearchImpl();
    }

    @Override
    public void createCompetitiveGame(String gameName, String owner, int maxNumberOfPlayers, Integer buyIn,
                                      Integer initialPlayerPot, Integer initialBet) {

        var game = Game.getByName(gameName);

        if (game.isPresent()) {
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
        Game game = Game.getByName(gameName)
                .orElseThrow(() -> new IllegalArgumentException("Game is not valid"));

        var user = User.getById(game.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("User is not valid, this should not happen"));

        GameRound gr = new GameRound();
        gr.setGameId(game.getId());
        gr.create();

        auditService.entry(Audit.builder().type(AuditType.GAME).owner(user).log("Game deleted").build());
    }

    @Override
    public void deleteGameByName(String gameName) {

        var game = Game.getByName(gameName)
                .orElseThrow(() -> new IllegalArgumentException("Game is not valid"));

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

        var game = Game.getByName(gameName)
                .orElseThrow(() -> new IllegalArgumentException("Game is not valid"));

        if (game.getUsers().size() >= game.getMaxPlayers()) {
            throw new IllegalStateException("Game is full");
        }

        game.addUser(user);

        DbSessionManager.getUnitOfWork().commit();

        auditService.entry(Audit.builder().type(AuditType.GAME).owner(user).log("User added to Game").build());
    }

    @Override
    public void removePlayerFromGame(String username, String gameName) {
        var user = User.getByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User is not valid"));

        var game = Game.getByName(gameName)
                .orElseThrow(() -> new IllegalArgumentException("Game is not valid"));

        game.removePlayer(user);

        DbSessionManager.getUnitOfWork().commit();

        auditService.entry(Audit.builder().type(AuditType.GAME).owner(user).log("User removed from Game").build());
    }

    @Override
    public void startGame(String gameName) {

        var game = Game.getByName(gameName)
                .orElseThrow(() -> new IllegalArgumentException("Game is not valid"));

        game.start();

        var user = User.getById(game.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("User is not valid, this should not happen"));

        auditService.entry(Audit.builder().type(AuditType.GAME).owner(user).log("Game started").build());

        game.setStatus(GameStatus.ONGOING);
        game.update();

        GameUser gu = new GameUser();
        gu.setGameId(game.getId());
        gu.setUserId(user.getId());
        gu.setCurrentPlayerPot(game.getInitialPlayerPot());
        gu.create();
    }

    @Override
    public void bet(String username, BigDecimal amount) {
        var user = User.getByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User for the specified username not found"));

        var gameUser = GameUser.getByUserId(user.getId());
        if (gameUser == null) {
            throw new IllegalStateException("Game user record does not exist");
        }

        Integer currentPlayerPot = gameUser.getCurrentPlayerPot();
        if (BigDecimal.valueOf(currentPlayerPot).subtract(amount).intValue() < BigDecimal.ZERO.intValue()) {
            throw new IllegalArgumentException("User cannot bet the specified amount. Insufficient balance");
        }

        // update player pot (subtract bet amount from balance)
        BigDecimal playerPotAfterBet = BigDecimal.valueOf(currentPlayerPot).subtract(amount);
        gameUser.setCurrentPlayerPot(playerPotAfterBet.intValue());
        gameUser.update();

        // update table pot balance (amount winner gets)
        GameRound gameRound = GameRound.getByGameId(gameUser.getGameId());
        gameRound.setTablePot(gameRound.getTablePot() + amount.intValue());
        gameRound.update();

        auditService.entry(Audit.builder().type(AuditType.GAME).owner(user).log("Player did bet. Amount=" + amount.intValue()).build());
    }

    @Override
    public void check(String username) {
        var user = User.getByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User for the specified username not found"));

        var gameUser = GameUser.getByUserId(user.getId());
        if (gameUser == null) {
            throw new IllegalStateException("Game user record does not exist");
        }

        gameUser.setStatus(GameUserStatus.CHECK);
        gameUser.update();

        auditService.entry(Audit.builder().type(AuditType.GAME).owner(user).log("Player did check").build());
    }

    @Override
    public void fold(String username) {
        var user = User.getByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User for the specified username not found"));

        var gameUser = GameUser.getByUserId(user.getId());
        if (gameUser == null) {
            throw new IllegalStateException("Game user record does not exist");
        }

        gameUser.setStatus(GameUserStatus.FOLD);
        gameUser.update();

        auditService.entry(Audit.builder().type(AuditType.GAME).owner(user).log("Player did fold").build());
    }

    @Override
    public void calculateWinner(String gameName) {
        var game = Game.getByName(gameName);

        if (game == null) {
            throw new IllegalArgumentException("Game does not exist");
        }

        var gameRound = GameRound.getByGameId(game.getId());
        if (gameRound == null) {
            throw new IllegalStateException("Game did not start or is in an invalid state");
        }

        List<DeckCard> tableCards = gameRound.getTableCards();

        List<GameUser> gameUsers = GameUser.getGameUsersByGameId(game.getId());

        List<GameUser> gameUsersToConsider = gameUsers.stream()
                .filter(gu -> gu.getStatus().equals(GameUserStatus.CHECK))
                .collect(Collectors.toList());

        int bestScore = 0;
        GameUser winner = null;
        for (GameUser u: gameUsersToConsider) {
            List<DeckCard> gameUserHand = u.getPlayerCards();
            gameUserHand.addAll(tableCards);

            HandResult playerHandResult = HandCalculator.score(gameUserHand);
            int playerScore = playerHandResult.calculateScore();
            if (playerScore > bestScore) {
                bestScore = playerScore;
                winner = u;
            }
        }

        User user = User.getById(winner.getUserId())
                .orElseThrow(() -> new IllegalStateException("Could not find the winner User account"));

        final String tableCardsStr = tableCards.stream().map(c -> c.toString()).collect(Collectors.joining(","));
        final String playerCardsStr = winner.getPlayerCards().stream().map(c -> c.toString()).collect(Collectors.joining(","));
        final String auditMsg = String.format("Player won the round with [%s]. Table cards[%s] | Player cards[%s] | Amount won: [%d]",
                PokerResult.getResultStringByScore(bestScore), tableCardsStr, playerCardsStr, gameRound.getTablePot());
        auditService.entry(Audit.builder().type(AuditType.GAME).owner(user).log(auditMsg).build());

        // update winner balance
        Integer playerPotAfterWin = winner.getCurrentPlayerPot() + gameRound.getTablePot();
        winner.setCurrentPlayerPot(playerPotAfterWin);
        winner.update();

        prepareNextRound(gameRound, gameUsers);
    }

    private void prepareNextRound(GameRound gameRound, List<GameUser> gameUsers) {
        // reset game round values (pot and cards)
        gameRound.setTablePot(0);
        gameRound.setCard1(null);
        gameRound.setCard2(null);
        gameRound.setCard3(null);
        gameRound.setCard4(null);
        gameRound.setCard5(null);
        gameRound.update();

        // reset game user hands
        gameUsers.forEach(u -> {
            u.setCard1(null);
            u.setCard2(null);
            u.setStatus(GameUserStatus.DEFAULT);
            u.update();
        });
    }
}
