package com.isec.pokercli.services.persistence.entity.game;

import com.isec.pokercli.services.persistence.entity.game.card.DeckCard;
import com.isec.pokercli.services.persistence.session.DbSessionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameUser {
    private Long id;
    private Long gameId;
    private Long userId;
    private Integer currentPlayerPot;
    private DeckCard card1;
    private DeckCard card2;
    private GameUserStatus status;

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

    public GameUserStatus getStatus() {
        return status;
    }

    public void setStatus(GameUserStatus status) {
        this.status = status;
    }

    public List<DeckCard> getPlayerCards() {
        List<DeckCard> result = new ArrayList<>();
        result.add(card1);
        result.add(card2);
        return result;
    }

    public static GameUser getByUserId(Long userId) {
        try {
            final String sql = "SELECT id, game_id, user_id, current_player_pot, card1, card2, status FROM game_user " +
                    "WHERE user_id=?";
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

    public static List<GameUser> getGameUsersByGameId(Long gameId) {
        List<GameUser> result = new ArrayList<>();
        try {
            final String sql = "SELECT id, game_id, user_id, current_player_pot, card1, card2, status FROM game_user " +
                    "WHERE game_id=?";
            Connection conn = DbSessionManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, gameId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                GameUser gu = map(rs);
                result.add(gu);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result.isEmpty()) {
            return null;
        }

        return result;
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
        gameUser.status = GameUserStatus.getByInt(rs.getInt(++i));

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
                    "card2=?, status=? WHERE id=?";

            Connection conn = DbSessionManager.getConnection();

            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            int i = 0;
            pstmt.setInt(++i, currentPlayerPot);
            pstmt.setString(++i, card1 != null ? card1.getDatabaseFormat() : null);
            pstmt.setString(++i, card2 != null ? card2.getDatabaseFormat() : null);
            pstmt.setInt(++i, status.getStatus());
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
