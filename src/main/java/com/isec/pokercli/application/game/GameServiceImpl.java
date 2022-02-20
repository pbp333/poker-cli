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
import com.isec.pokercli.services.persistence.session.UnitOfWork;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
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

        user.removeBalance(BigDecimal.valueOf(buyIn));

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

        // create a game user record for the game owner
        GameUser gu = new GameUser();
        gu.setGameId(game.getId());
        gu.setUserId(user.getId());
        gu.setCurrentPlayerPot(game.getInitialPlayerPot());
        gu.create();

        // remove balance from non-owner users
        game.getUsers().stream()
                .filter(u -> u.getId() != game.getOwnerId())
                .forEach(u -> u.removeBalance(BigDecimal.valueOf(game.getBuyIn())));

        DbSessionManager.getUnitOfWork().commit();
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
        var game = Game.getByName(gameName).orElseThrow(() -> new IllegalArgumentException("Game does not exist"));

        var gameRound = GameRound.getByGameId(game.getId());
        if (gameRound == null) {
            throw new IllegalStateException("Game did not start or is in an invalid state");
        }

        List<DeckCard> tableCards = gameRound.getTableCards();

        List<GameUser> gameUsers = GameUser.getGameUsersByGameId(game.getId());

        long invalidGameUsersCount = gameUsers.stream()
                .filter(gu -> gu.getStatus().equals(GameUserStatus.DEFAULT))
                .count();
        if (invalidGameUsersCount > 0) {
            throw new IllegalStateException("Cannot calculate game winner. Some players did not check or fold.");
        }

        List<GameUser> gameUsersToConsider = gameUsers.stream()
                .filter(gu -> gu.getStatus().equals(GameUserStatus.CHECK))
                .collect(Collectors.toList());

        int bestScore = 0;
        GameUser winner = null;
        HandResult winnerHandResult = null;
        for (GameUser u : gameUsersToConsider) {
            List<DeckCard> gameUserHand = u.getPlayerCards();
            gameUserHand.addAll(tableCards);

            HandResult playerHandResult = HandCalculator.score(gameUserHand);
            int playerScore = playerHandResult.calculateScore();
            if (playerScore > bestScore) {
                bestScore = playerScore;
                winner = u;
                winnerHandResult = playerHandResult;
            }
        }

        User user = User.getById(winner.getUserId())
                .orElseThrow(() -> new IllegalStateException("Could not find the winner User account"));

        final String tableCardsStr = tableCards.stream().map(c -> c.toString()).collect(Collectors.joining(","));
        final String playerCardsStr = winner.getPlayerCards().stream().map(c -> c.toString()).collect(Collectors.joining(","));
        final String auditMsg = String.format("Player won the round with [%s]. Table cards[%s] | Player cards[%s] | Amount won: [%d]",
                winnerHandResult.getPokerResult().name(), tableCardsStr, playerCardsStr, gameRound.getTablePot());

        auditService.entry(Audit.builder().type(AuditType.GAME).owner(user).log(auditMsg).build());

        updateWinnerBalance(gameRound, winner);

        prepareNextRound(gameRound, gameUsers);

        removePlayersWithNoBalance(gameUsers);

        checkForGameWinner(game);
    }

    private void updateWinnerBalance(GameRound gameRound, GameUser winner) {
        Integer playerPotAfterWin = winner.getCurrentPlayerPot() + gameRound.getTablePot();
        winner.setCurrentPlayerPot(playerPotAfterWin);
        winner.update();
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

    private void removePlayersWithNoBalance(List<GameUser> gameUsers) {
        gameUsers.stream()
                .filter(u -> u.getCurrentPlayerPot().intValue() <= 0)
                .forEach(u -> {
                    u.delete();

                    Optional<User> user = User.getById(u.getUserId());
                    if (user.isPresent()) {
                        auditService.entry(
                                Audit.builder()
                                        .type(AuditType.GAME)
                                        .owner(user.get())
                                        .log(String.format("Player '%s' lost. Removed from the game", user.get().getName()))
                                        .build()
                        );
                    }
                });
    }

    private void checkForGameWinner(Game game) {
        List<GameUser> gameUsers = GameUser.getGameUsersByGameId(game.getId());

        if (!gameUsers.isEmpty() && gameUsers.size() == 1) {

            GameUser gameUser = gameUsers.get(0);

            GameRound gameRound = GameRound.getByGameId(game.getId());

            User user = User
                    .getById(gameUser.getUserId())
                    .orElseThrow(() -> new IllegalStateException("Cannot find the game winner User account to update balance"));

            Integer buyIn = game.getBuyIn();
            Integer initialPlayerPot = game.getInitialPlayerPot();

            // Os PCJs são válidos apenas para esse jogo e são reconvertidos em PCs à mesma
            // taxa de conversão quando o jogo termina
            // Regra 3 simples
            // buy in -> initial player pot
            // x      -> game player pot
            BigDecimal amountWon = BigDecimal.valueOf((buyIn * gameUser.getCurrentPlayerPot()) / initialPlayerPot);

            user.addBalance(amountWon);

            game.setStatus(GameStatus.FINISHED);

            UnitOfWork.getInstance().commit();

            auditService.entry(
                    Audit.builder()
                            .type(AuditType.GAME)
                            .owner(user)
                            .log("Player won the game. Amount added to balance: " + amountWon)
                            .build()
            );

            // clean up
            gameUser.delete();
            gameRound.delete();
        }
    }
}
