package com.isec.pokercli.model.entity.user;

import com.isec.pokercli.model.entity.DbSessionManager;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class User implements IUser {

    Long id;
    String name;
    BigDecimal balance;
    BigDecimal virtualBalance;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public BigDecimal getVirtualBalance() {
        return virtualBalance;
    }

    public void setVirtualBalance(BigDecimal virtualBalance) {
        this.virtualBalance = virtualBalance;
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

    public static List<User> getAll() {
        List<User> result = new ArrayList<User>();
        try {
            final String sql = "SELECT id, name, balance, virtual_balance, created_at, updated_at FROM user";
            Connection conn = DbSessionManager.getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                User user = mapUserFromDb(rs);
                result.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static User getById(Long id) {
        User result = null;

        try {
            final String sql = "SELECT id, name, balance, virtual_balance, created_at, updated_at FROM user WHERE id=?";
            Connection conn = DbSessionManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                result = mapUserFromDb(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static User getByName(String name) {
        User result = null;

        try {
            final String sql = "SELECT id, name, balance, virtual_balance, created_at, updated_at FROM user WHERE name=?";
            Connection conn = DbSessionManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                result = mapUserFromDb(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private static User mapUserFromDb(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(Long.valueOf(rs.getInt(1)));
        user.setName(rs.getString(2));
        user.setBalance(rs.getBigDecimal(3));
        user.setVirtualBalance(rs.getBigDecimal(4));
        user.setCreatedAt(rs.getTimestamp(5).toLocalDateTime());
        user.setUpdatedAt(rs.getTimestamp(6).toLocalDateTime());
        return user;
    }

    public int create() {
        try {
            final String sql = "INSERT INTO user(name, balance, virtual_balance) VALUES (?, ?, ?)";

            Connection conn = DbSessionManager.getConnection();

            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, getName());
            pstmt.setBigDecimal(2, getBalance());
            pstmt.setBigDecimal(3, getVirtualBalance());
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
            final String sql = "UPDATE user SET name=?, balance=?, virtual_balance, updated_at where id = ?";

            Connection conn = DbSessionManager.getConnection();

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, getName());
            pstmt.setBigDecimal(2, getBalance());
            pstmt.setBigDecimal(3, getVirtualBalance());
            pstmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        try {
            final String sql = "DELETE FROM user where id = ?";

            Connection conn = DbSessionManager.getConnection();

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, getId());
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
