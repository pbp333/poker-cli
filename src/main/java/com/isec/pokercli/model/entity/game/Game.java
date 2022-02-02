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
        Game game = new Game();
        game.setId(Long.valueOf(rs.getInt(1)));
        game.setUserId(rs.getLong(2));
        final String gameTypeStr = rs.getString(3);
        game.setMaxPlayers(rs.getInt(4));
        game.setChips(rs.getInt(5));
        game.setCreatedAt(rs.getTimestamp(6).toLocalDateTime());
        game.setUpdatedAt(rs.getTimestamp(7).toLocalDateTime());
        final String statusStr = rs.getString(8);

        // map gametype and status to their enums
        Optional<GameType> gameType = GameType.getByString(gameTypeStr);
        if (gameType.isPresent()) {
            game.setGameType(gameType.get());
        }
        Optional<GameStatus> status = GameStatus.getByString(statusStr);
        if (status.isPresent()) {
            game.setStatus(status.get());
        }
        return game;
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

        public Builder withUserId(Long id) {
            this.userId = id;
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

    }

}
