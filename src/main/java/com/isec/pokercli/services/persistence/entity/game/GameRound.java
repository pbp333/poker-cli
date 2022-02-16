package com.isec.pokercli.services.persistence.entity.game;

import com.isec.pokercli.services.persistence.entity.game.card.DeckCard;
import com.isec.pokercli.services.persistence.session.DbSessionManager;

import java.sql.*;

public class GameRound {

    private Long id;
    private Long gameId;
    private Integer tablePot;
    private DeckCard card1;
    private DeckCard card2;
    private DeckCard card3;
    private DeckCard card4;
    private DeckCard card5;

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

    public Integer getTablePot() {
        return tablePot;
    }

    public void setTablePot(Integer tablePot) {
        this.tablePot = tablePot;
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

    public DeckCard getCard3() {
        return card3;
    }

    public void setCard3(DeckCard card3) {
        this.card3 = card3;
    }

    public DeckCard getCard4() {
        return card4;
    }

    public void setCard4(DeckCard card4) {
        this.card4 = card4;
    }

    public DeckCard getCard5() {
        return card5;
    }

    public void setCard5(DeckCard card5) {
        this.card5 = card5;
    }

    public static GameRound getByGameId(Long id) {
        try {
            final String sql = "SELECT id, game_id, table_pot, card1, card2, card3, card4, card5 FROM game_round " +
                    "WHERE game_id=?";
            Connection conn = DbSessionManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return map(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static GameRound map(ResultSet rs) throws SQLException {
        GameRound gr = new GameRound();

        int i = 0;

        gr.id = rs.getLong(++i);
        gr.gameId = rs.getLong(++i);
        gr.tablePot = rs.getInt(++i);
        gr.card1 = DeckCard.mapDeckCard(rs.getString(++i));
        gr.card2 = DeckCard.mapDeckCard(rs.getString(++i));
        gr.card3 = DeckCard.mapDeckCard(rs.getString(++i));
        gr.card4 = DeckCard.mapDeckCard(rs.getString(++i));
        gr.card5 = DeckCard.mapDeckCard(rs.getString(++i));

        return gr;
    }


    public void create() {
        try {
            final String sql = "INSERT INTO game_round(game_id, table_pot, card1, card2, card3, card4, card5) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            Connection conn = DbSessionManager.getConnection();

            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            int i = 0;
            pstmt.setLong(++i, gameId);
            pstmt.setInt(++i, tablePot != null ? tablePot : 0);
            pstmt.setString(++i, card1 != null ? card1.getDatabaseFormat() : null);
            pstmt.setString(++i, card2 != null ? card2.getDatabaseFormat() : null);
            pstmt.setString(++i, card3 != null ? card3.getDatabaseFormat() : null);
            pstmt.setString(++i, card4 != null ? card4.getDatabaseFormat() : null);
            pstmt.setString(++i, card5 != null ? card5.getDatabaseFormat() : null);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        try {
            final String sql = "UPDATE game_round SET table_pot=?, card1=?, " +
                    "card2=?, card3=?, card4=?, card5=? WHERE id=?";

            Connection conn = DbSessionManager.getConnection();

            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            int i = 0;
            pstmt.setInt(++i, tablePot);
            pstmt.setString(++i, card1 != null ? card1.getDatabaseFormat() : null);
            pstmt.setString(++i, card2 != null ? card2.getDatabaseFormat() : null);
            pstmt.setString(++i, card3 != null ? card3.getDatabaseFormat() : null);
            pstmt.setString(++i, card4 != null ? card4.getDatabaseFormat() : null);
            pstmt.setString(++i, card5 != null ? card5.getDatabaseFormat() : null);
            pstmt.setLong(++i, id);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        try {
            final String sql = "DELETE FROM game_round where id = ?";

            Connection conn = DbSessionManager.getConnection();

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
