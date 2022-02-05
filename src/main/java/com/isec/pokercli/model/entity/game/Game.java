package com.isec.pokercli.model.entity.game;

import com.isec.pokercli.model.entity.user.User;
import com.isec.pokercli.model.session.DbSessionManager;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Game {

    private Long id;
    private String name;
    private Long ownerId;
    private GameType gameType;
    private Integer maxPlayers;
    private Integer buyIn;
    private Integer initialPlayerPot;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private GameStatus status;
    private Integer bet;

    private List<User> users = new ArrayList<>();

    private GameUnitOfWork unitOfWork = GameUnitOfWork.getInstance();

    private Game() {

    }

    private Game(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.ownerId = builder.ownerId;
        this.gameType = builder.gameType;
        this.maxPlayers = builder.maxPlayers;
        this.buyIn = builder.buyIn;
        this.initialPlayerPot = builder.initialPlayerPot;
        this.status = builder.status;
        this.bet = builder.bet;

        unitOfWork.addCreated(this);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public GameType getGameType() {
        return gameType;
    }

    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public Integer getBuyIn() {
        return buyIn;
    }

    public Integer getInitialPlayerPot() {
        return initialPlayerPot;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public GameStatus getStatus() {
        return status;
    }

    public Integer getBet() {
        return bet;
    }

    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }

    public static List<Game> getAll() {
        List<Game> result = new ArrayList<>();
        try {
            final String sql = "SELECT id, name, owner_id, game_type, max_players, buy_in, initial_player_pot, created_at, " +
                    "updated_at, status, bet FROM game";
            Connection conn = DbSessionManager.getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Game game = map(rs);
                result.add(game);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        result.forEach(gm -> {
            gm.users = findUsersByGame(gm.id);
        });

        return result;
    }

    private static List<User> findUsersByGame(Long id) {
        List<User> result = new ArrayList<>();
        try {
            final String sql = "SELECT user_id FROM game_user WHERE game_id = ?";
            Connection conn = DbSessionManager.getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, id);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                result.add(User.getById(rs.getLong(1)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static List<Game> getByMinimumBalance(BigDecimal balance) {
        List<Game> result = new ArrayList<>();
        try {
            final String sql = "SELECT id, name, owner_id, game_type, max_players, buy_in, initial_player_pot, created_at, " +
                    "updated_at, status, bet FROM game WHERE initial_player_pot <= ? OR game_type = ?";
            Connection conn = DbSessionManager.getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, balance.intValue());
            st.setString(2, GameType.FRIENDLY.name());
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Game game = map(rs);
                result.add(game);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        result.forEach(gm -> {
            gm.users = findUsersByGame(gm.id);
        });

        return result;
    }

    public static Game getById(Long id) {
        try {
            final String sql = "SELECT id, name, owner_id, game_type, max_players, buy_in, initial_player_pot, " +
                    "created_at, updated_at, bet, status FROM game WHERE id  = ?";
            Connection conn = DbSessionManager.getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                var game = map(rs);
                game.users = findUsersByGame(game.id);
                return game;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Game getByName(String gameName) {
        try {
            final String sql = "SELECT id, name, owner_id, game_type, max_players, buy_in, initial_player_pot, " +
                    "created_at, updated_at, status, bet FROM game WHERE name  = ?";
            Connection conn = DbSessionManager.getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, gameName);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                var game = map(rs);
                game.users = findUsersByGame(game.id);
                return game;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Map from database ResultSet to Game entity object
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    private static Game map(ResultSet rs) throws SQLException {

        Game game = new Game();
        game.id = Long.valueOf(rs.getInt(1));
        game.name = rs.getString(2);
        game.ownerId = rs.getLong(3);
        game.gameType = GameType.getByString(rs.getString(4))
                .orElseThrow(() -> new IllegalStateException("Game Type is invalid"));
        game.maxPlayers = rs.getInt(5);
        game.buyIn = rs.getInt(6);
        game.initialPlayerPot = rs.getInt(7);
        game.createdAt = rs.getTimestamp(8).toLocalDateTime();
        game.updatedAt = rs.getTimestamp(9).toLocalDateTime();
        game.status = GameStatus.getByString(rs.getString(10))
                .orElseThrow(() -> new IllegalStateException("Game Status is invalid"));
        game.bet = rs.getInt(11);
        return game;

    }

    public int create() {
        try {
            final String sql = "INSERT INTO game(name, owner_id, game_type, max_players, buy_in, initial_player_pot, bet, status) " +
                    "VALUES (? ,?, ?, ?, ?, ?, ?)";

            Connection conn = DbSessionManager.getConnection();

            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, name);
            pstmt.setLong(2, ownerId);
            pstmt.setString(3, gameType.name());
            pstmt.setInt(4, maxPlayers);
            pstmt.setInt(5, initialPlayerPot);
            pstmt.setInt(6, bet);
            pstmt.setString(7, status.name());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            int key = rs.getInt(1);
            id = Long.valueOf(key);

            return key;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void update() {
        try {
            final String sql = "UPDATE game SET updated_at=?, status=? where id = ?";

            Connection conn = DbSessionManager.getConnection();

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(2, getStatus().name());
            pstmt.setLong(3, getId());
            pstmt.executeUpdate();

            updateGameUsers();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateGameUsers() {
        try {
            List<Long> userIdsAssociated = new ArrayList<>();

            final String sql = "SELECT user_id FROM game_user where game_id = ?";

            Connection conn = DbSessionManager.getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, this.id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                userIdsAssociated.add(rs.getLong(1));
            }

            List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());

            List<Long> usersAssociationToCreate = userIds.stream().filter(id -> !userIdsAssociated.contains(id))
                    .collect(Collectors.toList());
            List<Long> usersAssociationsToRemove = userIdsAssociated.stream().filter(id -> !userIds.contains(id))
                    .collect(Collectors.toList());

            usersAssociationToCreate.forEach(this::addUsersToGame);
            usersAssociationsToRemove.forEach(this::removeUsersFromGame);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void addUsersToGame(Long userAssociationToCreate) {
        try {
            final String sql = "INSERT INTO game_user(user_id, game_id) VALUES (? ,?)";

            Connection conn = DbSessionManager.getConnection();

            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setLong(1, userAssociationToCreate);
            pstmt.setLong(2, id);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void removeUsersFromGame(Long userAssociationToCreate) {
        try {
            final String sql = "DELETE FROM game_user WHERE user_id = ? AND game_id = ?";

            Connection conn = DbSessionManager.getConnection();

            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setLong(1, userAssociationToCreate);
            pstmt.setLong(2, id);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void delete() {
        try {
            final String sql = "DELETE FROM game where id = ?";

            Connection conn = DbSessionManager.getConnection();

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, getId());
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void markAsDeleted() {
        unitOfWork.addDeleted(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public void addUser(User user) {
        this.users.add(user);
        unitOfWork.addUpdated(this);
    }

    public void removePlayer(User user) {
        this.users.remove(user);
        unitOfWork.addUpdated(this);
    }

    /**
     * The game builder class
     */

    public static class Builder {
        private Long id;
        private String name;
        private Long ownerId;
        private GameType gameType;
        private Integer maxPlayers;
        private Integer buyIn;
        private Integer initialPlayerPot;
        private GameStatus status;
        private Integer bet;

        private Builder() {

        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder ownerId(Long ownerId) {
            this.ownerId = ownerId;
            return this;
        }

        public Builder gameType(GameType gameType) {
            this.gameType = gameType;
            return this;
        }

        public Builder maxPlayers(Integer maxPlayers) {
            this.maxPlayers = maxPlayers;
            return this;
        }

        public Builder buyIn(Integer buyIn) {
            this.buyIn = buyIn;
            return this;
        }

        public Builder initialPlayerPot(Integer initialPlayerPot) {
            this.initialPlayerPot = initialPlayerPot;
            return this;
        }

        public Builder status(GameStatus status) {
            this.status = status;
            return this;
        }

        public Builder bet(Integer bet) {
            this.bet = bet;
            return this;
        }

        public Game build() {
            return new Game(this);
        }

    }

    @Override
    public String toString() {
        return "Game{" +
                "name='" + name + '\'' +
                ", ownerId=" + ownerId +
                ", gameType=" + gameType +
                ", maxPlayers=" + maxPlayers +
                ", buyIn=" + buyIn +
                ", initialPlayerPot=" + initialPlayerPot +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", status=" + status +
                ", bet=" + bet +
                ", users=[" + String.join(", ", users.stream().map(User::getName).collect(Collectors.toList())) + "]" +
                '}';
    }
}
