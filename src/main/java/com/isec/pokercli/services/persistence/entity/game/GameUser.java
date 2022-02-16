package com.isec.pokercli.services.persistence.entity.game;

import com.isec.pokercli.services.persistence.entity.game.card.DeckCard;
import com.isec.pokercli.services.persistence.session.DbSessionManager;

import java.sql.*;

public class GameUser {
    private Long id;
    private Long gameId;
    private Long userId;
    private Integer currentPlayerPot;
    private DeckCard card1;
    private DeckCard card2;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getCurrentPlayerPot() {
        return currentPlayerPot;
    }

    public void setCurrentPlayerPot(Integer currentPlayerPot) {
        this.currentPlayerPot = currentPlayerPot;
    }

    public DeckCard getCard1() {
        return card1;
    }

    public void setCard1(DeckCard card1) {
        this.card1 = card1;
    }

    public DeckCard getCard2() {
        return card2;
    }

    public void setCard2(DeckCard card2) {
        this.card2 = card2;
    }

    public static GameUser getByUserId(Long userId) {
        try {
            final String sql = "SELECT id, game_id, user_id, current_player_pot, card1, card2 FROM game_user " +
                    "WHERE id=?";
            Connection conn = DbSessionManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return map(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static GameUser map(ResultSet rs) throws SQLException {
        GameUser gameUser = new GameUser();

        int i = 0;

        gameUser.id = rs.getLong(++i);
        gameUser.gameId = rs.getLong(++i);
        gameUser.userId = rs.getLong(++i);
        gameUser.currentPlayerPot = rs.getInt(++i);
        gameUser.card1 = DeckCard.mapDeckCard(rs.getString(++i));
        gameUser.card2 = DeckCard.mapDeckCard(rs.getString(++i));

        return gameUser;
    }

    public void create() {
        try {
            final String sql = "INSERT INTO game_user(game_id, user_id, current_player_pot, card1, card2) " +
                    "VALUES (?, ?, ?, ?, ?)";

            Connection conn = DbSessionManager.getConnection();

            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            int i = 0;
            pstmt.setLong(++i, gameId);
            pstmt.setLong(++i, userId);
            pstmt.setInt(++i, currentPlayerPot);
            pstmt.setString(++i, card1 != null ? card1.getDatabaseFormat() : null);
            pstmt.setString(++i, card2 != null ? card2.getDatabaseFormat() : null);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        try {
            final String sql = "UPDATE game_user SET current_player_pot=?, card1=?, " +
                    "card2=? WHERE id=?";

            Connection conn = DbSessionManager.getConnection();

            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            int i = 0;
            pstmt.setInt(++i, currentPlayerPot);
            pstmt.setString(++i, card1.getDatabaseFormat());
            pstmt.setString(++i, card2.getDatabaseFormat());
            pstmt.setLong(++i, id);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        try {
            final String sql = "DELETE FROM game_user where id = ?";

            Connection conn = DbSessionManager.getConnection();

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteByUserIdAndGameId(Long userId, Long gameId) {
        try {
            final String sql = "DELETE FROM game_user WHERE user_id = ? AND game_id = ?";

            Connection conn = DbSessionManager.getConnection();

            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setLong(1, userId);
            pstmt.setLong(2, gameId);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
