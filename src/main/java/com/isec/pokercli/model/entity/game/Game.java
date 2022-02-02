package com.isec.pokercli.model.entity.game;

import com.isec.pokercli.model.entity.DbSessionManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Game implements IGame {

    private Long id;
    private Long userId;
    private GameType gameType;
    private Integer maxPlayers;
    private Integer chips;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private GameStatus status;
    private Integer bet;

    public Game(Builder builder) {
        this.id = builder.id;
        this.userId = builder.userId;
        this.gameType = builder.gameType;
        this.maxPlayers = builder.bet;
        this.chips = builder.chips;
        this.status = builder.status;
        this.bet = builder.bet;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    @Override
    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    @Override
    public Integer getChips() {
        return chips;
    }

    public void setChips(Integer chips) {
        this.chips = chips;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public static List<Game> getAll() {
        List<Game> result = new ArrayList<Game>();
        try {
            final String sql = "SELECT id, user_id, game_type, max_players, chips, created_at, updated_at, status FROM game";
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

        return result;
    }

    public static Game getById(Long id) {
        Game result = null;
        try {
            final String sql = "SELECT id, user_id, game_type, max_players, chips, created_at, updated_at, status FROM game WHERE id  = ?";
            Connection conn = DbSessionManager.getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                result = map(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Map from database ResultSet to Game entity object
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    private static Game map(ResultSet rs) throws SQLException {
        Builder builder = new Game.Builder();
        builder.withId(Long.valueOf(rs.getInt(1)));
        builder.withUserId(rs.getLong(2));
        final String gameTypeStr = rs.getString(3);
        builder.withMaxPlayers(rs.getInt(4));
        builder.withChips(rs.getInt(5));
        builder.withCreatedAt(rs.getTimestamp(6).toLocalDateTime());
        builder.withUpdatedAt(rs.getTimestamp(7).toLocalDateTime());
        final String statusStr = rs.getString(8);

        // map gametype and status to their enums
        Optional<GameType> gameType = GameType.getByString(gameTypeStr);
        if (gameType.isPresent()) {
            builder.withGameType(gameType.get());
        }
        Optional<GameStatus> status = GameStatus.getByString(statusStr);
        if (status.isPresent()) {
            builder.withStatus(status.get());
        }
        return builder.build();
    }

    public int create() {
        try {
            final String sql = "INSERT INTO game(user_id, game_type, max_players, chips, status) VALUES (?, ?, ?, ?, ?)";

            Connection conn = DbSessionManager.getConnection();

            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setLong(1, getUserId());
            pstmt.setString(2, getGameType().name());
            pstmt.setInt(3, getMaxPlayers());
            pstmt.setInt(4, getChips());
            pstmt.setString(5, getStatus().name());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            int key = rs.getInt(1);
            setId(Long.valueOf(key));

            return key;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void update() {
        try {
            final String sql = "UPDATE game SET user_id=?, game_type=?, max_players=?, chips=?, updated_at=?, status=? where id = ?";

            Connection conn = DbSessionManager.getConnection();

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, getUserId());
            pstmt.setString(2, getGameType().name());
            pstmt.setInt(3, getMaxPlayers());
            pstmt.setInt(4, getChips());
            pstmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(6, getStatus().name());
            pstmt.setLong(7, getId());
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

    /**
     * The game builder class
     */

    public static class Builder {
        private Long id;
        private Long userId;
        private GameType gameType;
        private Integer maxPlayers;
        private Integer chips;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private GameStatus status;
        private Integer bet;

        public Builder() {
        }

        public Builder withId(Long id) {
            this.userId = id;

            return this;
        }
        public Builder withUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder withGameType(GameType type) {
            this.gameType = type;
            return this;
        }

        public Builder withMaxPlayers(Integer max) {
            this.maxPlayers = max;
            return this;
        }

        public Builder withCreatedAt(LocalDateTime dateTime) {
            this.createdAt = dateTime;
            return this;
        }

        public Builder withUpdatedAt(LocalDateTime dateTime) {
            this.updatedAt = dateTime;
            return this;
        }

        public Builder withChips(Integer chips) {
            this.chips = chips;
            return this;
        }


        public Builder withStatus(GameStatus status) {
            this.status = status;
            return this;
        }

        public Builder withBet(Integer bet) {
            this.bet = bet;
            return this;
        }

        public Game build() {
            return new Game(this);
        }

    }

}
